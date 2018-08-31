package com.ngxGeoBI.accounts;

import com.ngxGeoBI.accounts.model.Profile;
import com.ngxGeoBI.accounts.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserProfileController {
    @Autowired
    private UserProfileService profileService;

    /**
     * Api endpoint for retrieving the users profile
     * @param user_id can be user id or user name. The API intelligently determines.
     * @return
     */
    @RequestMapping(value="/user/{user_id}", method = RequestMethod.GET)
    public Profile getProfile(@PathVariable String user_id){
        Optional<Profile> profile;
        try {
            long user_Id = Long.parseLong(user_id);
            profile = profileService.findByUserId(user_Id);
        }catch(NumberFormatException ex){
            profile = profileService.findByUsername(user_id);
        }
        return profile.get();
    }

    /**
     * This api end point accepts both user id as well as username with multipart/form data implementation
     * so that image/Avatar can be uploaded. For implementation refer to :
     *
     * 1. SO question https://stackoverflow.com/questions/21329426/spring-mvc-multipart-request-with-json
     * also refer to following SO for mapping the Multipart file to byte[]
     * https://stackoverflow.com/questions/32067097/is-there-a-way-to-write-a-rest-controller-to-upload-file-using-spring-data-rest
     *
     * 2. Mkyong site: https://www.mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
     *
     */
    @RequestMapping(value="/user/{user_id}", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<?> updateProfile(@PathVariable String user_id, @RequestPart("profile") @Valid Profile partialProfile,
                                           @RequestPart(name ="avatar", required= false ) @Valid MultipartFile file){
        String message = "Profile updated successfully";
        MediaType mediaType = MediaType.IMAGE_PNG;


        if (!file.isEmpty()) {
            try {

               byte[] bytes = file.getBytes();
               partialProfile.setAvatar(bytes);
               partialProfile.setMediaType(mediaType);
            } catch (Exception e) {
                message = "Failed to upload Avatar "  + " => " + e.getMessage();
            }
        }
        try{
            long user_Id = Long.parseLong(user_id);
            profileService.updateByUserId(partialProfile, user_Id);
        }catch(NumberFormatException ex){
            profileService.updateByUsername(partialProfile, user_id);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
