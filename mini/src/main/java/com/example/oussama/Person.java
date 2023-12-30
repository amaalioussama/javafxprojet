    package com.example.oussama;

    public class Person {

        private String firstname;
        private String lastname;
        private String numberID;


        public Person(String firstname, String lastname, String numberID) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.numberID = numberID;
        }

        public String getFirstname() {
            return firstname;
        }


        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getNumberID() {
            return numberID;
        }

        public void setNumberID(String numberID) {
            this.numberID = numberID;
        }



        @Override
        public String toString() {
            return "Person [firstname=" + firstname + ", lastname=" + lastname +
                    ", numberID=" + numberID + "]";
        }
    }
