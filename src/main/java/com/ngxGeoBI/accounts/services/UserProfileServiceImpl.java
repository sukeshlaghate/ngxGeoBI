package com.ngxGeoBI.accounts.services;

import com.ngxGeoBI.accounts.model.Profile;
import com.ngxGeoBI.accounts.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public Optional<Profile> findByProfileId(Long profileId) {
        Optional<Profile> profile = Optional.ofNullable(profileRepository.getOne(profileId));
        return profile;
    }

    @Override
    public Optional<Profile> findByUsername(String username) {
        Optional<Profile> profile = profileRepository.findProfileByUser_Username(username);

        return profile;
    }

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        Optional<Profile> profile = profileRepository.findProfileByUser_Id(userId);
        return profile;
    }

    @Override
    public void updateByUsername(Profile updatedProfile, String userName) {
        Optional<Profile> profile = profileRepository.findProfileByUser_Username(userName);
        profile.ifPresent((profileTobeUpdated)-> this.updateProfile(profileTobeUpdated, updatedProfile));
    }

    @Override
    public void updateByUserId(Profile updatedProfile, Long user_id) {

        Optional<Profile> profile = profileRepository.findProfileByUser_Id(user_id);
        profile.ifPresent((profileTobeUpdated)-> this.updateProfile(profileTobeUpdated, updatedProfile));

    }

    private void  updateProfile(Profile profileTobeUpdated, Profile updatedProfile){
        // TODO: perform null checks to avoid overwriting?
        profileTobeUpdated.setAddress(updatedProfile.getAddress());
        profileTobeUpdated.setAddress2(updatedProfile.getAddress2());
        profileTobeUpdated.setAddress3(updatedProfile.getAddress3());
        profileTobeUpdated.setAvatar(updatedProfile.getAvatar());
        profileTobeUpdated.setBlog_link(updatedProfile.getBlog_link());
        profileTobeUpdated.setCity(updatedProfile.getCity());
        profileTobeUpdated.setLinkedin(updatedProfile.getLinkedin());
        profileTobeUpdated.setMediaType(updatedProfile.getMediaType());
        profileTobeUpdated.setPhone(updatedProfile.getPhone());
        profileTobeUpdated.setPin(updatedProfile.getPin());
        profileTobeUpdated.setState(updatedProfile.getState());

            /*  save() in Spring Data JPA is backed by merge() in plain JPA,
            therefore it makes your entity managed. Thus calling save()
            on an object with predefined id will update the corresponding
            database record rather than insert a new one
         */
        profileRepository.save( profileTobeUpdated);
    }
    // Removes row from table may not be needed as we are cascading the profile model it will be deleted with user being deleted
//    public void deleteProfile(Long id) {
//        profileRepository.delete(id);
//    }
}
