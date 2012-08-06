package org.openmrs.module.amrscustomization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Nicholas Ingosi Magaja
 * Date: 8/3/12
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomPersonAddress {
    private static final Log log = LogFactory.getLog(CustomPersonAddress.class);

    public void processAddresses(){

        AMRSCustomizationService service= Context.getService(AMRSCustomizationService.class);
        PersonService pservice=Context.getPersonService();

        String latitude="";
        String longitude="";
        int rec_count=0;


        String convertedlatitude="";
        String convertedlongitude="";
        Person person=new Person();

        PersonService ps=Context.getPersonService();

        //loop through all person ids and get person object
        Cohort personIdss=service.getPersonIds();

        Set<Integer> personSetIds=new HashSet<Integer>(personIdss.getMemberIds());
                for(Integer pids:personSetIds){

                    person=ps.getPerson(pids);

                    Set<PersonAddress> personAddress=person.getAddresses();

                        for(PersonAddress address:personAddress){

                            latitude =address.getLatitude().trim();

                                if(latitude.contains("°") && latitude.contains("'") && latitude.contains("\"")){

                                    if(latitude.startsWith("N") || latitude.startsWith("S")){

                                        longitude =address.getLongitude().trim();

                                            convertedlatitude=convertToDecimal(latitude);
                                            convertedlongitude=convertToDecimal(longitude);

                                                if(convertedlatitude !=null || convertedlongitude !=null)  {

                                                        address.setLatitude(convertedlatitude);
                                                        address.setLongitude(convertedlongitude);

                                                        pservice.savePerson(person);

                                                        //include a procedure to flash out the memory just in case of anything
                                                            rec_count++; //increament the counter after the saving
                                                            cleanupaftersaving(rec_count); //actual cleansing after saving 10 to 20 records

                                                        //empty the variables for the next iteration
                                                        latitude="";
                                                        longitude="";
                                                }
                                    }
                                }

                        }
                }


    }


    //this method does the actual conversion from either version to decimal format
    private String convertToDecimal(String str){

        //get the first character for N E S W

        Double finalCode=0.0;


        if(str.contains("°") && str.contains("'") && str.contains("\"")){

            char firstletter=str.charAt(0);

            //char[] codes={'N','W','S','E'};

            //get the string chain after first character
            String strsplit1=str.substring(1);//this the entire string excluding the direction


            String []split3=strsplit1.split("°");


            String degrees=getNumericValuesFromString(split3[0]);//this consists of degree part i.e only the numeric values
            String remainder1=split3[1];
            String [] getMin=remainder1.split("'");
            String minutes=getNumericValuesFromString(getMin[0]);
            String remainder2=getMin[1];
            String [] getSec=remainder2.split("\"");
            String seconds=getNumericValuesFromString(getSec[0]);

            Double degrees_part= Double.parseDouble(degrees);
            Double minutes_part= ((Double.parseDouble(minutes))/60);
            Double seconds_part=null;

            //check whether the second part is ss or ss.s
            if(seconds.length() == 3){  //check if the second part is sss
                String first_two_char=seconds.substring(0,2); //devide by 10 and get the first two digits
                String last_one_char=seconds.substring(seconds.length()-1);//get the last digit into a variable

                seconds_part=((Double.parseDouble(first_two_char))/(60*60))+((Double.parseDouble(last_one_char))/(1000*60*60)); //actual seconds computations
            }
            else{//seconds part should be 2 digits then
                seconds_part= ((Double.parseDouble(seconds))/(60*60)); //just compute as required
            }

            finalCode=degrees_part+minutes_part+seconds_part;
            //log.info("welcome here "+finalCode);

            if (firstletter == 'S' || firstletter == 'W') {
                finalCode = finalCode * -1;
            }
            // N and E do nothing about it
            return finalCode.toString();
        }
        else
            return null;


    }
    // method to clear memory just incase; after successful 20 saves
    private void cleanupaftersaving(int count){

        if(count % 20 == 0){
            Context.flushSession();
            Context.clearSession();
        }
    }
    //this method will pick only numeric values from the String supplied
    //this will enable us deal with coordinates like Latitude: N1A°1'131"Longitude: E34A°58'382" or Latitude: N1°1'131"Longitude: E34°58'382"
    //if any of the case above will be encountered, then the equivalent in decimal will be returned
    private String getNumericValuesFromString(String s){
        String value=new String();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        while (m.find()) {
            value=m.group();
        }
        return value;
    }
}


