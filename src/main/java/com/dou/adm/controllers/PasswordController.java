package com.dou.adm.controllers;

import org.springframework.stereotype.Controller;

/**
 * Created by Tu.Tran on 10/1/2018.
 */
@Controller
public class PasswordController {

//    @Value("${email.template.reset-password}")
//    private String emailResetTemplate;
//
//    @Value("${email.template.new-password}")
//    private String emailPasswordTemplate;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private JwtProvider jwtProvider;

 /*   @RequestMapping (value ="/forgot", method = RequestMethod.POST)
    public ResponseEntity<?> processForgotPassword(@RequestBody JwtUser jwtUser, HttpServletRequest request) {
        String userEmail = jwtUser.getEmail();
        BizResponse bizResponse = new BizResponse();
        // Lookup user in database by e-mail
        User user = userService.findUserByEmail(userEmail);

        if (user == null) {
            bizResponse.setSuccess(false);
            bizResponse.setMessage("We didn't find an account for that e-mail address.");
        } else {

            // Generate random 36-character string token for reset password
            user.setResetToken(UUID.randomUUID().toString());

            // Save token to database
            userService.updateResetToken(user);
            try {
                String uri = request.getRequestURI();
                String url = request.getRequestURL().toString();
                String appUrl = url.replace(uri,"") +"/reset?token=" + user.getResetToken();
                // Email message
                String recipient = user.getEmail();
                String subject = "[Reset Password Notification]You have requested reset password.";
                Map<String, Object> models = new HashMap();
                models.put("resetUrl", appUrl);
                models.put("userName", user.getLstNm());
                emailService.sendEmail(emailResetTemplate,recipient,subject,models);
                bizResponse.setSuccess(true);
                bizResponse.setMessage("A password reset link has been sent to " + userEmail);
            } catch (Exception ex) {
                bizResponse.setSuccess(false);
                bizResponse.setMessage(ex.getMessage());
            }
        }
        return ResponseEntity.ok(bizResponse);
    }

    ///executer token and generate new password after that send email
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody Map map, HttpServletResponse response) {

        BizResponse bizResponse = new BizResponse();
        User user = userService.findUserByResetToken(map.get("token").toString());

        if (user != null) { // Token found in DB
            // generate new password
            String randomPassword = UUID.randomUUID().toString();
            String newPassword  = jwtProvider.getSha256Hex(randomPassword).toLowerCase();
            try {
                // send new password to email
                String recipient = user.getEmail();
                String subject = "[Reset Password Notification]You have requested reset password.";
                Map models = new HashMap();
                models.put("email", user.getEmail());
                models.put("password", randomPassword);
                emailService.sendEmail(emailPasswordTemplate,recipient,subject,models);

                // update new password to DB
                // Set the reset token to null so it cannot be used again
                user.setUsrPwd(jwtProvider.generatePassword(newPassword));
                user.setResetToken(null);
                userService.updatePassword(user);

                JwtUser jwtUser = new JwtUser();
                jwtUser.setId(user.getUserId());
                jwtUser.setEmail(user.getEmail());

                response.setHeader("Authorization",jwtProvider.generateToken(jwtUser));
                bizResponse.setSuccess(true);
                bizResponse.setMessage("You have just reset password successfully !!!. The new password has sent to your email.");
                bizResponse.setDto(user);

            } catch (Exception ex) {
                bizResponse.setSuccess(false);
                bizResponse.setMessage(ex.getMessage());
            }

        } else { // Token not found in DB
            bizResponse.setSuccess(false);
            bizResponse.setMessage("Oops!  This is an invalid password reset link.");
        }

        return ResponseEntity.ok(bizResponse);
    }

    @RequestMapping(value = "/api/change-password", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody HashMap map) {
        BizResponse bizResponse = new BizResponse();
        if(userService.changePassword(map, jwtProvider)){
            bizResponse.setSuccess(true);
            bizResponse.setMessage("Your password has changed successfully !!!. Do you want to login right now.");
        }
        else {
            bizResponse.setSuccess(false);
            bizResponse.setMessage("Your password has not changed successfully !!!.");
        }
        return ResponseEntity.ok(bizResponse);
    }*/
}
