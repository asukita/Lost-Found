package com.pawhub.lostandfound.data;

import java.util.ArrayList;

import com.pawhub.lostandfound.transferobjects.Report;

public class DataCache {
        
        private static ArrayList<Report> allReports;
        
        public static void saveCache(ArrayList<Report> allReports){
                DataCache.allReports=allReports;
        }
        
        public static ArrayList<Report> getCache(){
                return DataCache.allReports;
        }

}