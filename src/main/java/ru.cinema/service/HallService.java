package ru.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.cinema.model.Hall;
import ru.cinema.model.Seat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
@Service
public class
HallService {
    private final Map<Integer, Hall> halls = new ConcurrentHashMap<>();

    public HallService() {
        halls.putIfAbsent(1, new Hall(1, 5, 6));
        halls.putIfAbsent(2, new Hall(2, 3, 4));
        halls.putIfAbsent(3, new Hall(3, 6, 7));
    }

    /**
     * Метод ищет зал по идентификационному номеру и возращает список мест.
     * @param id идентификационный номер зала.
     * @return List<Seat> места в зале.
     */
    public List<Seat> findById(int id) {
        return halls.get(id).getSeats();
    }

    /**
     * Метод возвращает общее количество мест в зале.
     * @param id идентификационный номер зала.
     * @return int общее количество мест в зале.
     */
    public int getNumOfSeats(int id) {
        return halls.get(id).getNumOfSeats();
    }

    /**
     * Метод возвращает количество рядов в зале.
     * @param id идентификационный номер зала.
     * @return int количество рядов в зале.
     */
    public int getRows(int id) {
        return halls.get(id).getRows();
    }

    /**
     * Метод возращает количество мест в ряду.
     * @param id идентификационный номер зала.
     * @return количество мест в ряду.
     */
    public int getCells(int id) {
        return halls.get(id).getCells();
    }
}
