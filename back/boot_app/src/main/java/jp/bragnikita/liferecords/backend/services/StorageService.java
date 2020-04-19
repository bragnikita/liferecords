package jp.bragnikita.liferecords.backend.services;

import java.util.List;

public interface StorageService {

    DayStorageService getDayStorageService(String day);

    List<String> getDaysWithContent(Integer max);

}
