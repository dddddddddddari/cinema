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

    public List<Seat> findById(int id) {
        return halls.get(id).getSeats();
    }


    public int getNumOfSeats(int id) {
        return halls.get(id).getNumOfSeats();
    }

    public int getRows(int id) {
        return halls.get(id).getRows();
    }

    public int getCells(int id) {
        return halls.get(id).getCells();
    }
}
