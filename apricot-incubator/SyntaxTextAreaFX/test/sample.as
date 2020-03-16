/* Click to Go to Web Page
Clicking on the specified symbol instance loads the URL in a new
browser window.
 
Instructions:
1. Replace  with the desired URL address.
   Keep the quotation marks ("").
*/
 
movieClip_2.addEventListener(MouseEvent.CLICK,
fl_ClickToGoToWebPage);
 
function fl_ClickToGoToWebPage(event:MouseEvent):void
{
     navigateToURL(new
URLRequest(""), "_blank");
}
