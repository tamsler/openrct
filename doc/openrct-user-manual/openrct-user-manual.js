/* Javascript functions */

function showImage(imageName)
{
	OpenWindow=window.open(imageName, "OpenRCT User Manual", "height=677,width=828,toolbar=no,scrollbars=no,menubar=no,location=no");
}

function showImageSize(imageName, w, h)
{
	OpenWindow=window.open(imageName, "OpenRCT User Manual", "height=" + h + ",width=" + w + ",toolbar=no,scrollbars=no,menubar=no,location=no");
}
