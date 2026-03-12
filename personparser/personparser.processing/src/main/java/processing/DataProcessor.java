package processing;

import data.FileData;
import data.PersonRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessor {

    public ProcessingResult process(FileData filedata){
        List<PersonRecord> records = filedata.getRecords();
        Map<String, Integer> nameCounts = new HashMap<>();
        Map<String, Integer> surnameCounts = new HashMap<>();

        for(PersonRecord record : records){
            for(String name : record.getNames()){
                nameCounts.put(name, nameCounts.getOrDefault(name, 0) + 1);
            }
            String surname = record.getSurname();
            surnameCounts.put(surname, surnameCounts.getOrDefault(surname, 0) + 1);
        }

        String mostCommonName = findMostCommon(nameCounts);
        String mostCommonSurname = findMostCommon(surnameCounts);

        return new ProcessingResult(records.size(), mostCommonName, mostCommonSurname);
    }

    private String findMostCommon(Map<String, Integer> counts){
        String mostCommon = null;
        int maxCount = 0;
        for(Map.Entry<String, Integer> entry : counts.entrySet()){
            if(entry.getValue() > maxCount){
                mostCommon = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return mostCommon;
    }
}
