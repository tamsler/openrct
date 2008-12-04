// $Id: MakeSingle.h,v 1.3 2003/05/08 20:34:43 thomas Exp $

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

#ifndef __MAKESINGLE_H__
#define __MAKESINGLE_H__

// MakeSingle template takes a class as a template
// argument and makes it a Singleton class.
template<typename Type>
class MakeSingle : public Type {

protected:

    inline MakeSingle() { };
    static MakeSingle* instance_;
    
public:

    // Static inline method Instance
    // This method controls the instantiation
    // process so that only one instance of the
    // class is created. For all the other invocations,
    // the existing class object pointer is returned.
    inline static MakeSingle* instance() {

        if (instance_ == 0) {

            instance_ = new MakeSingle<Type>;
        }

        return instance_;
    }
};

// Initialize static instance member
template <typename Type>
MakeSingle<Type>* MakeSingle<Type>::instance_ = 0;

#endif // _MAKESINGLE_H_
