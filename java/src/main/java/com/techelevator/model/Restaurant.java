package com.techelevator.model;




        public class Restaurant {
            private int id;
            private String name;
            private String address;
            private String image;
            private double rating;
            private String phone;
            private String website;
            private boolean OpenNow;
            private String hoursInterval;
            private boolean servesBeer;
            private boolean servesWine;
            private String placeId;

            public String getPlaceId() {
                return placeId;
            }

            public void setPlaceId(String placeId) {
                this.placeId = placeId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public double getRating() {
                return rating;
            }

            public void setRating(double rating) {
                this.rating = rating;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getWebsite() {
                return website;
            }

            public void setWebsite(String website) {
                this.website = website;
            }

            public boolean OpenNow() {
                return OpenNow;
            }

            public void setOpenNow(boolean openNow) {
                OpenNow = openNow;
            }

            public String getHoursInterval() {
                return hoursInterval;
            }

            public void setHoursInterval(String hoursInterval) {
                this.hoursInterval = hoursInterval;
            }

            public boolean isServesBeer() {
                return servesBeer;
            }

            public void setServesBeer(boolean servesBeer) {
                this.servesBeer = servesBeer;
            }

            public boolean isServesWine() {
                return servesWine;
            }

            public void setServesWine(boolean servesWine) {
                this.servesWine = servesWine;
            }
// Getters and setters...
        }







