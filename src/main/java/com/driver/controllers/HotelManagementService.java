package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class HotelManagementService {


    HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();
    public String addHotel(Hotel hotel) {
        //You need to add a hotel to the database
        //in case the hotelName is null or the hotel Object is null return an empty a FAILURE
        //In case somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.

        if(hotel.getHotelName() == null||hotel.getHotelName() == "" || hotel == null){
            return "FAILURE";
        }
        List<String> hotelsByname = hotelManagementRepository.getAllHotelName();
        if(hotelsByname.contains(hotel.getHotelName())){
            return "Failure";
        }
        hotelManagementRepository.saveHotel(hotel);
        return "SUCCESS";

    }

    public Integer addUser(User user) {

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user

        hotelManagementRepository.saveUser(user);
        return user.getaadharCardNo();
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        //We are having a new facilities that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible

        Hotel hotel = hotelManagementRepository.getHotelByName(hotelName);
        List<Facility> oldFacilities = hotel.getFacilities();

        for(Facility facility : newFacilities){
            if(!oldFacilities.contains(facility)){
                oldFacilities.add(facility);
            }
        }

        hotelManagementRepository.updateFacilities(oldFacilities,hotel);

        return hotelManagementRepository.getHotelByName(hotelName);


    }

    public String getHotelWithMostFacilities() {
        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with at least 1 facility return "" (empty string)

        int maxNumberOfFacility = hotelManagementRepository.getMaxFacility();

        List<String> hotelList = hotelManagementRepository.getAllHotelNameByNumberOfFacility(maxNumberOfFacility);


        if(hotelList.size() == 1){
            return hotelList.get(0);

        }
        else{
             return getHotelNameLexicographically(hotelList);
        }

    }

    private String getHotelNameLexicographically(List<String> hotelList) {

        //sorting the list in lexicographically order
        int n = hotelList.size();
        String ans = hotelList.get(0);
        for (int i = 0 ; i < n - 1 ; i++ ){
            for (int j = i + 1 ; j < n ; j++){
                if(hotelList.get(i).compareTo(hotelList.get(j)) > 0){
                    ans = hotelList.get(j);
                }
            }
        }
        return ans;

    }

    public int bookARoom(Booking booking) {
        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there aren't enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

        String bookingId = UUID.randomUUID().toString();

        int bookingAadharCard = booking.getBookingAadharCard();
        int noOfRooms = booking.getNoOfRooms();
        String bookingPersonName = booking.getBookingPersonName();
        String hotelName = booking.getHotelName();

        Hotel hotel = hotelManagementRepository.getHotelByName(hotelName);
        int hotelrooms = hotel.getAvailableRooms();

        if(noOfRooms > hotelrooms){
            return -1;
        }
        hotelManagementRepository.saveBooking(bookingId,booking);
        int pricOfRoomPerNight = hotel.getPricePerNight();
        int totalPrice = pricOfRoomPerNight*noOfRooms;

        return totalPrice;
    }

    public int getBookings(Integer aadharCard) {
        List<Booking> bookingList = hotelManagementRepository.getAllBookings();
        int count = 0;
        for(Booking booking : bookingList){
            if(booking.getBookingAadharCard() == aadharCard){
                count++;
            }
        }
        return count;
    }
}
