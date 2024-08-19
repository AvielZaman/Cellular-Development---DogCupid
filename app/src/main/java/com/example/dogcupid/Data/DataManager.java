package com.example.dogcupid.Data;

import com.example.dogcupid.Models.Chat;
import com.example.dogcupid.Models.Dog;

import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {

    public static HashMap<String, Dog> getDogs() {
        HashMap<String, Dog> allDogs = new HashMap<>();

        allDogs.put("User 1",
                new Dog()
                        .setDogName("Buddy")
                        .setDogAge(3)
                        .setDogBreed("Golden Retriever")
                        .setGender("Male")
                        .setDescription("Friendly and energetic, loves to play fetch.")
                        .setLatitude(32.112923)
                        .setLongitude(34.8182147)
                        .setImageUri("https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Golden_Retriever_standing_Tucker.jpg/640px-Golden_Retriever_standing_Tucker.jpg")
                        .setOwnerName("Omer")
                        .setOwnerPhone("0524763312"));

        allDogs.put("User 2",
                new Dog()
                        .setDogName("Bella")
                        .setDogAge(5)
                        .setDogBreed("Labrador Retriever")
                        .setGender("Female")
                        .setDescription("Calm and affectionate, great with kids.")
                        .setLatitude(32.112223)
                        .setLongitude(34.8187147)
                        .setImageUri("https://www.purina.co.uk/sites/default/files/styles/square_medium_440x440/public/2022-09/labrador%20retriever.jpg?h=324ab4fd&itok=l2Si-Qkl")
                        .setOwnerName("Jacob")
                        .setOwnerPhone("0525893312"));

        allDogs.put("User 3",
                new Dog()
                        .setDogName("Charlie")
                        .setDogAge(2)
                        .setDogBreed("Beagle")
                        .setGender("Male")
                        .setDescription("Curious and loves to explore, enjoys long walks.")
                        .setLatitude(32.113923)
                        .setLongitude(34.8184147)
                        .setImageUri("https://www.buldog.co.il/wp-content/uploads/2020/08/%D7%92%D7%95%D7%A8-%D7%91%D7%99%D7%92%D7%9C.jpg")
                        .setOwnerName("Ronen")
                        .setOwnerPhone("0524742012"));

        allDogs.put("User 4",
                new Dog()
                        .setDogName("Daisy")
                        .setDogAge(4)
                        .setDogBreed("Poodle")
                        .setGender("Male")
                        .setDescription("Smart and obedient, easy to train.")
                        .setLatitude(32.112983)
                        .setLongitude(34.8182107)
                        .setImageUri("https://blv.co.il/wp-content/uploads/2022/11/Toy_poodle_1667406661.jpg")
                        .setOwnerName("Arnon")
                        .setOwnerPhone("0529888072"));

        allDogs.put("User 5",
                new Dog()
                        .setDogName("Max")
                        .setDogAge(1)
                        .setDogBreed("German Shepherd")
                        .setGender("Male")
                        .setDescription("Loyal and protective, ideal for security.")
                        .setLatitude(32.112973)
                        .setLongitude(34.8172147)
                        .setImageUri("https://www.cheni.co.il/wp-content/uploads/2020/12/german-shepherd.jpg")
                        .setOwnerName("Hila")
                        .setOwnerPhone("0524766767"));

        allDogs.put("User 6",
                new Dog()
                        .setDogName("Lucy")
                        .setDogAge(6)
                        .setDogBreed("Bulldog")
                        .setGender("Female")
                        .setDescription("Gentle and friendly, loves to relax.")
                        .setLatitude(32.113923)
                        .setLongitude(34.8162147)
                        .setImageUri("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Racib%C3%B3rz_2007_082.jpg/1200px-Racib%C3%B3rz_2007_082.jpg")
                        .setOwnerName("Ohad")
                        .setOwnerPhone("0524020312"));

        allDogs.put("User 7",
                new Dog()
                        .setDogName("Rocky")
                        .setDogAge(2)
                        .setDogBreed("Boxer")
                        .setGender("Male")
                        .setDescription("Energetic and playful, great for active families.")
                        .setLatitude(32.112923)
                        .setLongitude(34.8152147)
                        .setImageUri("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVH_NPcb20FJfEBf1B6D_FsXxgDuBDiL-48g&s")
                        .setOwnerName("Avi")
                        .setOwnerPhone("0524763366"));

        allDogs.put("User 8",
                new Dog()
                        .setDogName("Molly")
                        .setDogAge(7)
                        .setDogBreed("Shih Tzu")
                        .setGender("Female")
                        .setDescription("Loving and affectionate, enjoys being pampered.")
                        .setLatitude(32.119923)
                        .setLongitude(34.8112147)
                        .setImageUri("https://www.forbes.com/advisor/wp-content/uploads/2023/11/shih-tzu-temperament.jpeg.jpg")
                        .setOwnerName("Tal")
                        .setOwnerPhone("0544768312"));

        allDogs.put("User 9",
                new Dog()
                        .setDogName("Duke")
                        .setDogAge(4)
                        .setDogBreed("Rottweiler")
                        .setGender("Male")
                        .setDescription("Strong and confident, excellent guard dog.")
                        .setLatitude(32.118923)
                        .setLongitude(34.8188147)
                        .setImageUri("https://pet-ins.net/wp-content/uploads/2020/10/Rottweiler2.jpg")
                        .setOwnerName("Noa")
                        .setOwnerPhone("0544880312"));

        allDogs.put("User 10",
                new Dog()
                        .setDogName("Zoe")
                        .setDogAge(3)
                        .setDogBreed("Cocker Spaniel")
                        .setGender("Female")
                        .setDescription("Friendly and sociable, enjoys being around people.")
                        .setLatitude(32.112963)
                        .setLongitude(34.8182547)
                        .setImageUri("https://revivolution.co.il/wp-content/uploads/2022/11/english-cocker-spaniel-5937751-scaled.jpg")
                        .setOwnerName("Almog")
                        .setOwnerPhone("0505328750"));

        return allDogs;
    }

}
