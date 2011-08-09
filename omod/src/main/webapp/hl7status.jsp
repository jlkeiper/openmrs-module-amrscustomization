<%@ page import="org.openmrs.api.context.Context" %><%@ page import="org.openmrs.hl7.HL7InQueue" %><%@ page import="java.util.Date" %><%
Integer WARNING_LEVEL = 45;
Integer CRITICAL_LEVEL = 120;
String ALLOWED_IPS = ",41.191.226.125,192.168.5.231,134.68.31.227,";
String USERNAME = "hl7_monitor"; // user with only "View HL7 Inbound Queue" privilege
String PASSWORD = "oICu812!";

// Restrict access by remote address
if (ALLOWED_IPS.indexOf(","+request.getRemoteAddr()+",") == -1) {
  response.setStatus(401); // Unauthorized
  return;
}

Long delay = null;
HL7InQueue hl7 = null;
String errorMessage = "";
try {
  Context.authenticate(USERNAME, PASSWORD);
  // n = Context.getHL7Service().countHL7InQueue(HL7_STATUS_PENDING,null);
  hl7 = Context.getHL7Service().getNextHL7InQueue();
  delay = (hl7 != null) ? ((new Date().getTime()) - hl7.getDateCreated().getTime()) / 1000 : 0;
} catch (Exception e) {
  errorMessage = e.getMessage();
}
if (delay == null)
  out.println("Status: CRITICAL - " + errorMessage);
else if (delay < WARNING_LEVEL)
  out.println("Status: OK - Oldest queue entry is " + delay + " seconds old.");
else if (delay < CRITICAL_LEVEL)
  out.println("Status: WARNING - Oldest queue entry is " + delay + " seconds old.");
else
  out.println("Status: CRITICAL - Oldest queue entry is " + delay + " seconds old.");
%>