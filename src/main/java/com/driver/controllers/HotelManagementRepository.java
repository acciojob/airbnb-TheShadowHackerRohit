package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HotelManagementRepository {

    Map<String,Hotel> hotelMap ;

    Map<Integer,User> userMap ;

    Map<String,Booking> bookingMap;

    public HotelManagementRepository() {
        hotelMap = new HashMap<>();
        userMap = new HashMap<>();
        bookingMap = new HashMap<>();
    }



    public void saveHotel(Hotel hotel) {
        hotelMap.put(hotel.getHotelName(),hotel);
    }

    public List<String> getAllHotelName() {
        return new ArrayList<>(hotelMap.keySet());
    }




    public void saveUser(User user) {
        userMap.put(user.getaadharCardNo(),user);
    }

    public Hotel getHotelByName(String hotelName) {
        return hotelMap.get(hotelName);
    }

    public void updateFacilities(List<Facility> newFacilities, Hotel hotel) {
        hotel.setFacilities(newFacilities);
        saveHotel(hotel);
    }

    public int getMaxFacility() {
        int max = Integer.MIN_VALUE;
        for(String hotelName : hotelMap.keySet()){
            Hotel hotel = hotelMap.get(hotelName);
            int size = hotel.getFacilities().size();
            max = Math.max(max,size);
        }
        return max;
    }

    public List<String> getAllHotelNameByNumberOfFacility(int max) {
        List<String> hotelList = new ArrayList<>();
        for(String hotelName : hotelMap.keySet()){
            Hotel hotel = hotelMap.get(hotelName);
            int size = hotel.getFacilities().size();
            if(size == max){
                hotelList.add(hotelName);
            }
        }
        return hotelList;
    }

    public void saveBooking(String bookingId, Booking booking) {
        bookingMap.put(bookingId,booking);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingMap.values());
    }

//    public User getUserByAdhaar(String bookingPersonName) {
//        return userMap.get(bookingPersonName);
//    }
}
