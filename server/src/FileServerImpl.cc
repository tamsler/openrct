/* $Id: FileServerImpl.cc,v 1.32 2004/10/23 06:06:45 thomas Exp $ */

/*
 *
 *   OpenRCT - Open Remote Collaboration Tool
 *
 *   Copyright (c) 2000 by Thomas Amsler
 * 
 *   This file is part of OpenRCT.
 *
 *   OpenRCT is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   OpenRCT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenRCT; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

#include "FileServerImpl.h"

// Local Method:
void
FileServer_i::init()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Init the textpad id and ref arrays
    for(int i = 0; i < MAX_NUMBER_DOWNLOADS; i++) {
      
        pr_file_download_info_refs[i] = NULL;
    }
}


// Local Method:
CORBA::Boolean
FileServer_i::store_file_download_info(FileDownloadInfo *a_fdi)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Enter critical section
    RctMutex::file.lock();
    
    for(int i = 0; i < MAX_NUMBER_DOWNLOADS; i++) {

        if(NULL == pr_file_download_info_refs[i]) {

            pr_file_download_info_refs[i] = a_fdi;
            // Exit cricical section
            RctMutex::file.unlock();
            return 1;
        }
        else if(pr_file_download_info_refs[i]->is_stopped) {

            delete pr_file_download_info_refs[i];
            pr_file_download_info_refs[i] = a_fdi;
            // Exit cricical section
            RctMutex::file.unlock();
            return 1;
	
        }
    }      

    // Exit cricical section
    RctMutex::file.unlock();
    return 0;
}


// Local Method:
CORBA::Boolean
FileServer_i::fetch(const char *a_file_id,
                    BinaryFile_out a_file_data,
                    FileAccess a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // Vars for the file name and location
    CORBA::String_var l_file_path;
    CORBA::String_var l_file_name;
    CORBA::String_var l_file_path_name;

    // Compose the query string
    CORBA::String_var l_query;

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    switch(a_type) {

    case RCT_FA_COMMON:

        l_query = UTIL::comp_string(SELECT_FILE_FROM_ID,
                                    a_file_id,
                                    "'",
                                    END);

        l_res = l_db_server->exec_db_query(l_query);

        if(l_db_server->has_result_one_tuple(l_res)) {

            l_file_path = l_db_server->db_getvalue(l_res, 0, RCT_F_LOCATION);
            l_file_name = l_db_server->db_getvalue(l_res, 0, RCT_F_NAME);

            // Now we can create the full path with file name
            l_file_path_name = UTIL::comp_string((char *)l_file_path,
                                                 PATH_TOKEN,
                                                 (char *)l_file_name,
                                                 END);

            // Freeing memory
            l_db_server->db_clear(l_res);
        }
        else {

            cerr << "ERROR: FileID not known!" << endl;

            // Freeing memory
            l_db_server->db_clear(l_res);

            // Create the empty BinaryFile
            a_file_data = new BinaryFile();

            return 0;
        }

        break;

    case RCT_FA_COURSE_CONTENT:

        // Since we know that the course content stored file
        // is named a certain way, we don't need to query the db
        // File name follows this pattern:
        // cc_CCX where X is an integer between 0 and N
        l_file_path_name = UTIL::comp_string(DATA_DIR,
                                             PATH_TOKEN,
                                             MODULE_PRE_CC,
                                             a_file_id,
                                             END);
        
        break;

    default:

        cerr << "ERROR: Wrong file access type!" << endl;

        // Create the empty BinaryFile
        a_file_data = new BinaryFile();

        return 0;
    }


    // File descriptor
    int l_fd;

    // Open the file from given location
    if((l_fd = open((char *)l_file_path_name, O_RDONLY)) == -1) {

        // There was a problem with opening the file
        cerr << "ERROR: Could not open the file" << endl;

        // Create the empty BinaryFile
        a_file_data = new BinaryFile();

        return 0;
    }

    // Create file stat to access file specific data
    struct stat l_st;
    fstat(l_fd, &l_st);

    // Create octet buffer to store file data
    CORBA::Octet *l_buf;
    l_buf = BinaryFile::allocbuf(l_st.st_size);

    // Now we can read the file's data
    if(read(l_fd, l_buf, l_st.st_size) == -1) {

        // There was a problem with reading the file
        cerr << "ERROR:: Could not read the file" << endl;

        // Create the empty BinaryFile
        a_file_data = new BinaryFile();
        
        return 0;
    }

    // Closing the file descriptor
    close(l_fd);

    // Instanciate a file data seq
    // The last argument signals that this call will dealocate the buffer
    a_file_data = new BinaryFile(l_st.st_size, l_st.st_size, l_buf, 1);

    // Everything went ok
    return 1;
}

// Local Method:
CORBA::Boolean
FileServer_i::send(const char *a_id,
                   const CORBA::WChar *a_alias,
                   const char *a_name,
                   const char *a_source,
                   const char *a_mime_type,
                   const char *a_class_name,
                   const char *a_assembly_name,
                   const char *a_user_id,
                   const char *a_user_alias,
                   const char *a_version,
                   const char *a_module,
                   CORBA::Long a_assembly_type,
                   ObjPermission a_permission,
                   CORBA::Boolean a_has_id,
                   const BinaryFile &a_file_data)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();

    // Checking if file id was supplied
    if(!a_has_id) {

        // If there was no file id supplied, we have to request one
        // Not implemented yet!
        cout << "INFO: FileServer.send() : No File Id Was Supplied!" << endl;
    }

    // Compse the path and file name string
    CORBA::String_var l_file_path_name;
    l_file_path_name = UTIL::comp_string(DATA_DIR,
                                         PATH_TOKEN,
                                         a_name,
                                         END);

    // Create/Open file
    ofstream l_data_file(l_file_path_name, ios::out | ios::binary);

    if(!l_data_file) {

        cerr << "ERROR: Cannot open " << a_name << " for output!" << endl;
        return 0;
    }

    // Write data to file
    l_data_file.write((const char *)a_file_data.get_buffer(),
                      a_file_data.length());

    // Close file
    l_data_file.close();

    // Getting the class_id from class_name
    CORBA::String_var l_class_id;
    l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);

    // Convert the permission to a string
    CORBA::String_var l_permission;
    l_permission = UTIL::int_to_str(a_permission);

    CORBA::String_var l_assembly_id;
    CORBA::String_var l_trans;

    // Make the file name safe fore db entry
    char* l_file_name_esc =
        l_db_server->db_escape_string(a_name,
                                      strlen(a_name));

    // Getting string length of alias
    size_t l_str_size = wcstombs(NULL, a_alias, 0) + 1;

    // Create the multi byte alias string
    char l_alias_multi_byte[l_str_size];

    if(ERROR <= wcstombs(l_alias_multi_byte, a_alias, l_str_size)) {
        
        cerr << "ERROR: Could not convert a character!" << endl;
    }

    // Make the alias safe fore db entry
    char* l_alias_multi_byte_esc =
        l_db_server->db_escape_wstring(l_alias_multi_byte,
                                       l_str_size - 1);
    
    // TYPE TEAM
    if(a_assembly_type == ASSEMBLY_TYPE_TEAM) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                  a_assembly_name);
        l_trans = UTIL::comp_string(INS_FILE,
                                    a_id, "','",
                                    l_alias_multi_byte_esc, "','",
                                    l_file_name_esc, "','",
                                    DATA_DIR, "','",
                                    a_source, "','",
                                    a_mime_type, "','",
                                    (char *)l_class_id, "','",
                                    (char *)l_assembly_id, "','",
                                    DEFAULT_GROUP, "','",
                                    a_user_id, "','",
                                    a_module, "','",
                                    "true", "','",
                                    (char *)l_permission, "','",
                                    "now", "','",
                                    a_version, "')", END);
    }
    // TYPE GROUP
    else if(a_assembly_type == ASSEMBLY_TYPE_GROUP) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                     a_assembly_name);
         
        l_trans = UTIL::comp_string(INS_FILE,
                                    a_id, "','",
                                    l_alias_multi_byte_esc, "','",
                                    l_file_name_esc, "','",
                                    DATA_DIR, "','",
                                    a_source, "','",
                                    a_mime_type, "','",
                                    (char *)l_class_id, "','",
                                    DEFAULT_TEAM, "','",
                                    (char *)l_assembly_id, "','",
                                    a_user_id, "','",
                                    a_module, "','",
                                    "true", "','",
                                    (char *)l_permission, "','",
                                    "now", "','",
                                    a_version, "')", END);
    }
    // TYPE ERROR
    else {

        cerr << "ERROR: Wrong assembly type!" << endl;
    }

    // Insert file into DB
    l_db_server->exec_db_insert(l_trans);

    // Cleaning up escaped file name
    delete l_file_name_esc;
    delete l_alias_multi_byte_esc;
     
    return 1;
}


// IDL Method
char*
FileServer_i::get_index() {
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    CORBA::String_var l_id;
    CORBA::String_var l_index;
    
    l_index = l_db_server->get_file_index();
    l_id = UTIL::comp_string(RCT_FILE,
                             (char *)l_index,
                             END);

    return l_id._retn();
}


// IDL Method:
void
FileServer_i::start_transfer(const char *a_name)
    throw(CORBA::SystemException, RCT::FileServer::FileIOException)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Compse the path and file name string
    CORBA::String_var l_file_path_name;
    l_file_path_name = UTIL::comp_string(DATA_DIR,
                                         PATH_TOKEN,
                                         a_name,
                                         END);

    // Create file
    ofstream l_data_file(l_file_path_name, ios::out | ios::binary);

    if(!l_data_file) {
        
        cerr << "ERROR: Cannot open " << a_name << " for output!" << endl;
        throw RCT::FileServer::FileIOException("RCT_EXCEPTION: Cannot open file");
    }
    else {

        // Close file
        l_data_file.close();
    }
}



// IDL Method:
void
FileServer_i::end_transfer(const char *a_id,
                           const CORBA::WChar *a_alias,
                           const char *a_name,
                           const char *a_mime_type,
                           const char *a_class_name,
                           const char *a_assembly_name,
                           const char *a_user_id,
                           const char *a_user_alias,
                           const char *a_version,
                           const char *a_module,
                           CORBA::Long a_type,
                           ObjPermission a_permission,
			   const char *a_length)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    FtpServerS *l_ftp_server = FtpServerS::instance();
    SoundServerS *l_sound_server = SoundServerS::instance();

    // Start DB Entry
    
    // Getting the class_id from class_name
    CORBA::String_var l_class_id;
    l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);

    // Convert the permission to a string
    CORBA::String_var l_permission;
    l_permission = UTIL::int_to_str(a_permission);

    CORBA::String_var l_assembly_id;
    CORBA::String_var l_trans;

    // Make the file name safe fore db entry
    char* l_file_name_esc =
        l_db_server->db_escape_string(a_name,
                                      strlen(a_name));

    // Getting string length of alias
    size_t l_str_size = wcstombs(NULL, a_alias, 0) + 1;

    // Create the multi byte alias string
    char l_alias_multi_byte[l_str_size];

    if(ERROR <= wcstombs(l_alias_multi_byte, a_alias, l_str_size)) {
        
        cerr << "ERROR: Could not convert a character!" << endl;
    }

    // Make the alias safe fore db entry
    char* l_alias_multi_byte_esc =
        l_db_server->db_escape_wstring(l_alias_multi_byte,
                                       l_str_size - 1);
    
    // TEAM
    if((FTP_TEAM_UPLOAD == a_type) ||
       (SOUND_TEAM == a_type)) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                  a_assembly_name);
        l_trans = UTIL::comp_string(INS_FILE,
                                    a_id, "','",
                                    l_alias_multi_byte_esc, "','",
                                    l_file_name_esc, "','",
                                    DATA_DIR, "','",
                                    FILE_SERVER_SOURCE, "','",
                                    a_mime_type, "','",
                                    (char *)l_class_id, "','",
                                    (char *)l_assembly_id, "','",
                                    DEFAULT_GROUP, "','",
                                    a_user_id, "','",
                                    a_module, "','",
                                    "true", "','",
                                    (char *)l_permission, "','",
                                    a_length, "','",
                                    "now", "','",
                                    a_version, "')", END);
    }
    // GROUP
    else if((FTP_GROUP_UPLOAD == a_type) ||
            (SOUND_GROUP == a_type)) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                     a_assembly_name);
         
        l_trans = UTIL::comp_string(INS_FILE,
                                    a_id, "','",
                                    l_alias_multi_byte_esc, "','",
                                    l_file_name_esc, "','",
                                    DATA_DIR, "','",
                                    FILE_SERVER_SOURCE, "','",
                                    a_mime_type, "','",
                                    (char *)l_class_id, "','",
                                    DEFAULT_TEAM, "','",
                                    (char *)l_assembly_id, "','",
                                    a_user_id, "','",
                                    a_module, "','",
                                    "true", "','",
                                    (char *)l_permission, "','",
                                    a_length, "','",
                                    "now", "','",
                                    a_version, "')", END);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Insert file into DB
    l_db_server->exec_db_insert(l_trans);

    // Cleaning up escaped file name
    delete l_file_name_esc;
    delete l_alias_multi_byte_esc;
     
    // End DB Entry

    if((FTP_TEAM_UPLOAD == a_type) || (FTP_GROUP_UPLOAD == a_type)) {

        l_ftp_server->upload(a_id,
                             a_alias,
                             a_name,
                             a_mime_type,
                             a_class_name,
                             a_assembly_name,
                             a_user_id,
                             a_user_alias,
                             a_version,
                             a_length,
                             a_type,
                             a_permission);
    }
    else if((SOUND_TEAM == a_type) || (SOUND_GROUP == a_type)) {

        l_sound_server->send(a_id,
                             a_alias,
                             a_name,
                             a_mime_type,
                             a_class_name,
                             a_assembly_name,
                             a_user_id,
                             a_user_alias,
                             a_version,
                             a_length,
                             a_type,
                             a_permission);
    }
}


// IDL Method:
void
FileServer_i::cancel_transfer(const char *a_name)
    throw(CORBA::SystemException, RCT::FileServer::FileIOException)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Compse the path and file name string
    CORBA::String_var l_file_path_name;
    l_file_path_name = UTIL::comp_string(DATA_DIR,
                                         PATH_TOKEN,
                                         a_name,
                                         END);


    if(-1 == remove(l_file_path_name)) {

        cerr << "ERROR: Cannot remove " << a_name << "!" << endl;
        throw RCT::FileServer::FileIOException("RCT_EXCEPTION: Cannot remove file");
    }
}


// IDL Method:
void
FileServer_i::send_packet(const char *a_name,
                          const FilePacket &a_packet)
    throw(CORBA::SystemException, RCT::FileServer::FileIOException)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Compse the path and file name string
    CORBA::String_var l_file_path_name;
    l_file_path_name = UTIL::comp_string(DATA_DIR,
                                         PATH_TOKEN,
                                         a_name,
                                         END);

    // Create file
    ofstream l_data_file(l_file_path_name, ios::app | ios::binary);

    if(!l_data_file) {
        
        cerr << "ERROR: Cannot open " << a_name << " for output!" << endl;
        throw RCT::FileServer::FileIOException("RCT_EXCEPTION: Cannot open file");
    }

    l_data_file.write((const char *)a_packet.packet.get_buffer(),
                      a_packet.packet.length());
    
    // Close file
    l_data_file.close();
}

// IDl Method:
void
FileServer_i::start_download(const char *a_id,
			     const char *a_user_id,
			     const char *a_user_alias,
                             const char *a_class_name,
                             const char *a_assembly_name,
                             const char *a_version,
			     CORBA::Long a_type,
			     CORBA::Long a_packet_size)
    throw(CORBA::SystemException, RCT::FileServer::FileIOException)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    FileDownloadInfo* fdi = new FileDownloadInfo(a_id,
                                                 a_user_id,
                                                 a_user_alias,
                                                 a_class_name,
                                                 a_assembly_name,
                                                 a_type,
                                                 a_packet_size);

    // Store file download info
    if(store_file_download_info(fdi)) {
      
        // Create thread to run chunk and send data
        pthread_t p_tid;

        // We have to deal with both cases where we download a regular file
        // and also where we download a course content
        if(CC_DOWNLOAD == a_type) {

            pthread_create(&p_tid, NULL, chunk_and_send_data_cc, fdi);
        }
        else {

            pthread_create(&p_tid, NULL, chunk_and_send_data, fdi);
        }
    }
    else {
      
        throw RCT::FileServer::FileIOException("RCT_EXCEPTION: Max number of concurrent downloads reached");
    }
}


// IDl Method:
void
FileServer_i::cancel_download(const char *a_id,
                              const char *a_user_id,
                              const char *a_class_name,
                              const char *a_assembly_name)
    throw(CORBA::SystemException, RCT::FileServer::FileIOException)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Enter critical section
    RctMutex::file.lock();
    
    for(int i = 0; i < MAX_NUMBER_DOWNLOADS; i++) {

        if((0 == strcmp(a_id, pr_file_download_info_refs[i]->id)) &&
           (0 == strcmp(a_user_id, pr_file_download_info_refs[i]->user_id)) &&
           (0 == strcmp(a_class_name, pr_file_download_info_refs[i]->class_name)) &&
           (0 == strcmp(a_assembly_name, pr_file_download_info_refs[i]->assembly_name))) {

            pr_file_download_info_refs[i]->is_stopped = TRUE;
	    
	    // Exit cricical section
	    RctMutex::file.unlock();
            
	    return;
        }
    
    }
	
    // Exit cricical section
    RctMutex::file.unlock();
}


// Function:
// This function is called in the start_downlaod thread
void*
chunk_and_send_data(void* a_arg)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Cast the method argument
    FileDownloadInfo* l_file_download_info = (FileDownloadInfo*)a_arg;
    
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Determine potential timeout interval between sending packages
    // and setting l_timeout variable
    CORBA::ULong l_timeout;

    if(PACKET_SIZE_LAN == l_file_download_info->packet_size) {
      
      l_timeout = 2;
    }
    else if(PACKET_SIZE_DSL == l_file_download_info->packet_size) {
      
      l_timeout = 4;
    }
    else if(PACKET_SIZE_MOD == l_file_download_info->packet_size) {
      
      l_timeout = 4;
    }
    else {
      
      cerr << "ERROR: FileServerImpl.chunk_and_send_data wrong package size!" << endl;
    }

    // Vars for the file name and location
    CORBA::String_var l_file_path;
    CORBA::String_var l_file_name;
    CORBA::String_var l_file_path_name;

    // Compose the query string
    CORBA::String_var l_query;

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    l_query = UTIL::comp_string(SELECT_FILE_FROM_ID,
                                (char *)l_file_download_info->id,
                                "'",
                                END);
    
    l_res = l_db_server->exec_db_query(l_query);
    
    if(l_db_server->has_result_one_tuple(l_res)) {
        
        l_file_path = l_db_server->db_getvalue(l_res, 0, RCT_F_LOCATION);
        l_file_name = l_db_server->db_getvalue(l_res, 0, RCT_F_NAME);
      
        // Now we can create the full path with file name
        l_file_path_name = UTIL::comp_string((char *)l_file_path,
                                             PATH_TOKEN,
                                             (char *)l_file_name,
                                             END);
      
        // Freeing memory
        l_db_server->db_clear(l_res);
    }
    else {
      
        cerr << "ERROR: FileID not known!" << endl;
      
        // Freeing memory
        l_db_server->db_clear(l_res);
    }
    
    // File descriptor
    int l_fd;

    // Open the file from given location
    if((l_fd = open((char *)l_file_path_name, O_RDONLY)) == -1) {

        // There was a problem with opening the file
        cerr << "ERROR: Could not open the file" << endl;
    }

    // Create file stat to access file specific data
    struct stat l_st;
    fstat(l_fd, &l_st);

    // Create octet buffer to store file data
    // Regular size packet
    CORBA::Octet *l_buf = BinaryPacket::allocbuf(l_file_download_info->packet_size);  

    // Get the file size
    int l_file_size = l_st.st_size;

    // Keep track of how much data has been sent so far
    int l_data_sent = 0;
    int l_remaining_data = 0;

    while((!l_file_download_info->is_stopped) &&
          (l_data_sent < l_file_size)) {
	
        // Create the file packet message
        FilePacketMessage l_file_packet_msg;
	l_file_packet_msg.base_msg.version = "rct";
        l_file_packet_msg.base_msg.user_alias = l_file_download_info->user_alias;
        l_file_packet_msg.base_msg.class_name = l_file_download_info->class_name;
	l_file_packet_msg.base_msg.permission = UNCLASSIFIED;
        l_file_packet_msg.base_msg.type = l_file_download_info->type;
        l_file_packet_msg.id = l_file_download_info->id;
        l_file_packet_msg.packet_type = FILE_DOWNLOAD_PACKET;
    
        if((FTP_TEAM_DOWNLOAD == l_file_download_info->type) ||
           (FTP_ARCHIVE_DOWNLOAD == l_file_download_info->type) ||
	   (SOUND_TEAM_PLAY == l_file_download_info->type) ||
           (SOUND_ARCHIVE_PLAY == l_file_download_info->type)) {
      
            l_file_packet_msg.base_msg.team_name = l_file_download_info->assembly_name;
        }
        else if((FTP_GROUP_DOWNLOAD == l_file_download_info->type) ||
		(SOUND_GROUP_PLAY == l_file_download_info->type)) {
      
            l_file_packet_msg.base_msg.group_name = l_file_download_info->assembly_name;
        }
        else {
      
            cerr << "ERROR: Wrong message type!" << endl;
        }
    
      
        l_remaining_data = l_file_size - l_data_sent;

        // Determine the packet size
        if(l_remaining_data < l_file_download_info->packet_size) {
	
            BinaryFile::freebuf(l_buf);
            l_buf = BinaryPacket::allocbuf(l_remaining_data);
      
            // Now we can read the data packet
            if(read(l_fd, l_buf, l_remaining_data) == -1) {

                // There was a problem with reading the file
                cerr << "ERROR:: Could not read the file" << endl;
            }

            l_data_sent += l_remaining_data;
	    
            // Add data to package
            l_file_packet_msg.packet = BinaryPacket(l_remaining_data,
                                                    l_remaining_data,
                                                    l_buf,
                                                    0);
        }
        else {
      
            // Now we can read the data packet
            if(read(l_fd, l_buf, l_file_download_info->packet_size) == -1) {

                // There was a problem with reading the file
                cerr << "ERROR:: Could not read the file" << endl;
            }

            l_data_sent += l_file_download_info->packet_size;
	    
            // Add data to package
            l_file_packet_msg.packet = BinaryPacket(l_file_download_info->packet_size,
                                                    l_file_download_info->packet_size,
                                                    l_buf,
                                                    0);
        }

        // At this point we send the packet to the client
        l_session_server->push_file_msg(l_file_packet_msg, atoi(&l_file_download_info->user_id[USER_ID_OFFSET]));

	// Sleep for some time depending on the client's bandwidth
	sleep(l_timeout);

    } // End While Loop

    // Now we are done sending packages and send the client the END message
    // Create the file packet message
    FilePacketMessage l_file_packet_msg;
    l_file_packet_msg.base_msg.user_alias = l_file_download_info->user_alias;
    l_file_packet_msg.base_msg.class_name = l_file_download_info->class_name;
    l_file_packet_msg.base_msg.type = l_file_download_info->type;
    l_file_packet_msg.id = l_file_download_info->id;

    // Check if download was stopped/canceled
    if(l_file_download_info->is_stopped) {

        l_file_packet_msg.packet_type = FILE_DOWNLOAD_STOPPED;
    }
    else {
      
        l_file_packet_msg.packet_type = FILE_DOWNLOAD_END;
    }
    
    if((FTP_TEAM_DOWNLOAD == l_file_download_info->type) ||
       (FTP_ARCHIVE_DOWNLOAD == l_file_download_info->type) ||
       (SOUND_TEAM_PLAY == l_file_download_info->type) ||
       (SOUND_ARCHIVE_PLAY == l_file_download_info->type)) {
      
        l_file_packet_msg.base_msg.team_name = l_file_download_info->assembly_name;
    }
    else if((FTP_GROUP_DOWNLOAD == l_file_download_info->type) ||
	    (SOUND_GROUP_PLAY == l_file_download_info->type)) {
        
        l_file_packet_msg.base_msg.group_name = l_file_download_info->assembly_name;
    }
    else {
        
        cerr << "ERROR: Wrong message type!" << endl;
    }
    
    // Sending end packet to the client
    l_session_server->push_file_msg(l_file_packet_msg, atoi(&l_file_download_info->user_id[USER_ID_OFFSET]));
    
    // Free up buffer
    BinaryPacket::freebuf(l_buf);
    
    // Mark the file download info as finished
    l_file_download_info->is_stopped = 1;

    // Closing the file descriptor
    close(l_fd);
  
    return NULL;
}


// Function:
// This function is called in the start_downlaod thread
// and when we download course content
void*
chunk_and_send_data_cc(void* a_arg)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Cast the method argument
    FileDownloadInfo* l_file_download_info = (FileDownloadInfo*)a_arg;
    
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Determine potential timeout interval between sending packages
    // and setting l_timeout variable
    CORBA::ULong l_timeout;

    if(PACKET_SIZE_LAN == l_file_download_info->packet_size) {
      
      l_timeout = 2;
    }
    else if(PACKET_SIZE_DSL == l_file_download_info->packet_size) {
      
      l_timeout = 4;
    }
    else if(PACKET_SIZE_MOD == l_file_download_info->packet_size) {
      
      l_timeout = 4;
    }
    else {
      
      cerr << "ERROR: FileServerImpl.chunk_and_send_data wrong package size!" << endl;
    }

    // Vars for the file name and location
    CORBA::String_var l_file_path_name;

    // Since we know that the course content stored file
    // is named a certain way, we don't need to query the db
    // File name follows this pattern:
    // cc_CCX where X is an integer between 0 and N
    l_file_path_name = UTIL::comp_string(DATA_DIR,
                                         PATH_TOKEN,
                                         MODULE_PRE_CC,
                                         (char *)l_file_download_info->id,
                                         END);
    
    // File descriptor
    int l_fd;

    // Open the file from given location
    if((l_fd = open((char *)l_file_path_name, O_RDONLY)) == -1) {

        // There was a problem with opening the file
        cerr << "ERROR: Could not open the file" << endl;
    }

    // Create file stat to access file specific data
    struct stat l_st;
    fstat(l_fd, &l_st);

    // Create octet buffer to store file data
    // Regular size packet
    CORBA::Octet *l_buf = BinaryPacket::allocbuf(l_file_download_info->packet_size);  

    // Get the file size
    int l_file_size = l_st.st_size;

    // Keep track of how much data has been sent so far
    int l_data_sent = 0;
    int l_remaining_data = 0;

    while((!l_file_download_info->is_stopped) &&
          (l_data_sent < l_file_size)) {

        // Create the file packet message
        FilePacketMessage l_file_packet_msg;
        l_file_packet_msg.base_msg.user_alias = l_file_download_info->user_alias;
        l_file_packet_msg.base_msg.class_name = l_file_download_info->class_name;
        l_file_packet_msg.base_msg.type = l_file_download_info->type;
        l_file_packet_msg.id = l_file_download_info->id;
        l_file_packet_msg.packet_type = FILE_DOWNLOAD_PACKET;
      
        l_remaining_data = l_file_size - l_data_sent;

        // Determine the packet size
        if(l_remaining_data < l_file_download_info->packet_size) {
	
            BinaryFile::freebuf(l_buf);
            l_buf = BinaryPacket::allocbuf(l_remaining_data);
      
            // Now we can read the data packet
            if(read(l_fd, l_buf, l_remaining_data) == -1) {

                // There was a problem with reading the file
                cerr << "ERROR:: Could not read the file" << endl;
            }

            l_data_sent += l_remaining_data;
	    
            // Add data to package
            l_file_packet_msg.packet = BinaryPacket(l_remaining_data,
                                                    l_remaining_data,
                                                    l_buf,
                                                    0);
        }
        else {
      
            // Now we can read the data packet
            if(read(l_fd, l_buf, l_file_download_info->packet_size) == -1) {

                // There was a problem with reading the file
                cerr << "ERROR:: Could not read the file" << endl;
            }

            l_data_sent += l_file_download_info->packet_size;
	    
            // Add data to package
            l_file_packet_msg.packet = BinaryPacket(l_file_download_info->packet_size,
                                                    l_file_download_info->packet_size,
                                                    l_buf,
                                                    0);
        }

        // At this point we send the packet to the client
        l_session_server->push_file_msg(l_file_packet_msg, atoi(&l_file_download_info->user_id[USER_ID_OFFSET]));

	// Sleep for some time depending on the client's bandwidth
	sleep(l_timeout);

    } // End While Loop

    // Now we are done sending packages and send the client the END message
    // Create the file packet message
    FilePacketMessage l_file_packet_msg;
    l_file_packet_msg.base_msg.user_alias = l_file_download_info->user_alias;
    l_file_packet_msg.base_msg.class_name = l_file_download_info->class_name;
    l_file_packet_msg.base_msg.type = l_file_download_info->type;
    l_file_packet_msg.id = l_file_download_info->id;

    // Check if download was stopped/canceled
    if(l_file_download_info->is_stopped) {

        l_file_packet_msg.packet_type = FILE_DOWNLOAD_STOPPED;
    }
    else {
      
        l_file_packet_msg.packet_type = FILE_DOWNLOAD_END;
    }
    
    // Sending end packet to the client
    l_session_server->push_file_msg(l_file_packet_msg, atoi(&l_file_download_info->user_id[USER_ID_OFFSET]));
    
    // Free up buffer
    BinaryPacket::freebuf(l_buf);
    
    // Mark the file download info as finished
    l_file_download_info->is_stopped = 1;

    // Closing the file descriptor
    close(l_fd);
  
    return NULL;
}
