package org.openmrs.module.amrscustomization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;

import java.util.HashSet;
import java.util.Set;

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


        String convertedlatitude="";
        String convertedlongitude="";
        Person person;

        PersonService ps=Context.getPersonService();

        //loop through all person ids and get person object
        Cohort personIdss=service.getPersonIds();

        Set<Integer> personSetIds=new HashSet<Integer>(personIdss.getMemberIds());
           log.info("latitudes needed to be converted in amrs "+personSetIds.size());


    }



    private String convertToDecimal(String str){

        //get the first character for N E S W

        Double finalCode=0.0;


        if(str.contains("°") && str.contains("'") && str.contains("\"")){

            char firstletter=str.charAt(0);

            //char[] codes={'N','W','S','E'};

            //get the string chain after first character
            String strsplit1=str.substring(1);


            String []split3=strsplit1.split("°");


            String degrees=split3[0];
            String remainder1=split3[1];
            String [] getMin=remainder1.split("'");
            String minutes=getMin[0];
            String remainder2=getMin[1];
            String [] getSec=remainder2.split("\"");
            String seconds=getSec[0];

            finalCode=(Double) (Double.parseDouble(degrees)+((Double.parseDouble(minutes))/60)+((Double.parseDouble(seconds))/(60*60)));
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

}
