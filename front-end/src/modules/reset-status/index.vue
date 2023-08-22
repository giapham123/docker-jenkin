<template>
  <v-content>
    <v-container fluid fill-height>
      <v-layout align-center justify-center>
        <v-flex xs12 sm8 md4 lg4>
          <v-card class="elevation-12">
            <v-toolbar dark color="primary">
              <v-toolbar-title>Reset Password</v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <v-form ref="form" lazy-validation>
                <p class="text-sm-left">{{ message }}</p>
                <v-text-field
                  ref="oldpassword"
                  v-model="user.oldpassword"
                  type="password"
                  label="Old Password"
                  prepend-icon="lock"
                  required
                  v-on:keyup.13="changePassword"
                />
                <v-text-field
                  ref="newpassword"
                  v-model="user.newpassword"
                  type="password"
                  label="New Password"
                  prepend-icon="lock"
                  required
                  v-on:keyup.13="changePassword"
                />
                <v-text-field
                  ref="confirmpassword"
                  v-model="user.confirmpassword"
                  type="password"
                  label="Confirm Password"
                  prepend-icon="lock"
                  required
                  v-on:keyup.13="changePassword"
                />
                <v-btn color="success" primary large @click="goToLogin">
                  Login
                </v-btn>
                <v-btn
                  v-if="success"
                  color="success"
                  primary
                  large
                  @click="changePassword"
                >
                  Change Password
                </v-btn>
                <v-btn
                  v-if="!success"
                  color="error"
                  primary
                  large
                  @click="goToResetPass"
                >
                  Reset Password
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>
  </v-content>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import SHA256 from 'sha256';

export default {
  components: {},
  data: () => ({
    token: '',
    user: {
      newpassword: '',
      confirmpassword: '',
      oldpassword: ''
    },
    loading: false,
    showReset: false
  }),
  computed: {
    ...mapGetters('resetstatus', ['message', 'success', 'signal', 'userId'])
  },

  watch: {
    signal() {
      if (this.success) {
        this.showSuccessMsg(this.message);
      } else {
        this.showErrorMsg(this.message);
      }
    }
  },

  mounted() {
    this.token = this.$route.query.token;
    this.resetStatus({
      token: this.token
    });
  },

  methods: {
    ...mapActions('resetstatus', ['resetStatus', 'changePasswords']),
    ...mapActions('global', ['showErrorMsg', 'showSuccessMsg']),

    goToLogin() {
      this.$router.push('/login');
    },

    goToResetPass() {
      this.$router.push('/forgotpassword');
    },

    changePassword() {
      if (!this.user.oldpassword) {
        this.showErrorMsg('Please input old password!');
        this.$refs.oldpassword.focus();
      } else if (!this.user.newpassword) {
        this.showErrorMsg('Please input new password!');
        this.$refs.newpassword.focus();
      } else if (!this.user.confirmpassword) {
        this.showErrorMsg('Please input confirm password!');
        this.$refs.confirmpassword.focus();
      } else if (this.user.newpassword != this.user.confirmpassword) {
        this.showErrorMsg('Password does not match!');
      } else {
        this.changePasswords({
          oldPassword: SHA256(this.user.oldpassword),
          newPassword: SHA256(this.user.newpassword),
          userId: this.userId
        });
      }
    }
  }
};
</script>

<style scoped></style>
