<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/jquery-1.11.1.js"></script>
<!------ Include the above in your HEAD tag ---------->

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<body>
<div style="padding-top: 200px"></div>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="text-center">
                        <h3><i class="fa fa-lock fa-4x"></i></h3>
                        <h2 class="text-center">Reset Password</h2>
                        <p>Hi ${userName},</p>
                        <p>We received a request to reset the password associated with this email address. If you made this request, please follow the instructions below.</p>
                        <p>Click the link below to reset your password using our secure server:</p>
                        <a href="${resetUrl}" >${resetUrl}</a>
                        <p>If you did not request to have your password reset, you can safely ignore this email.</p>
                        <p>Rest assured that your customer account is safe.</p>
                        <p>If clicking the link doesn't seem to work, you can copy and paste the link into your browsers address</p>
                        <p>window, or retype it there. Once you have returned to Default Demo Site, we will provide instructions for resetting your password.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>