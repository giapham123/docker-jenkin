<template>
  <v-content class="home-app-wrapper">
    <v-container fluid fill-height>
      <v-layout align-center justify-center>
        <v-flex xs12 sm8 md4 lg4>
          <v-card class="elevation-12">
            <v-toolbar dark color="#009688">
              <v-toolbar-title>Register</v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <v-form ref="form" lazy-validation>
                <v-text-field
                  ref="username"
                  v-model="user.username"
                  :rules="usernameRules"
                  prepend-icon="account_circle"
                  label="User Name"
                  required
                  autofocus
                  v-on:keyup.13="subRegister"
                />
                <v-text-field
                  ref="password"
                  v-model="user.password"
                  :rules="passwordRules"
                  type="password"
                  label="Password"
                  prepend-icon="lock"
                  required
                  v-on:keyup.13="subRegister"
                />
                <v-text-field
                  ref="confirmpassword"
                  v-model="user.confirmpassword"
                  :rules="confirmpasswordRules"
                  type="password"
                  label="Confirm Password"
                  prepend-icon="lock"
                  required
                  v-on:keyup.13="subRegister"
                />
                <v-text-field
                  ref="myfullname"
                  v-model="user.myFullName"
                  :rules="myFullNameRules"
                  prepend-icon="person"
                  label="My Full Name"
                  required
                  v-on:keyup.13="subRegister"
                />
                <v-text-field
                  ref="mafccode"
                  v-model="user.mafcCode"
                  :rules="mafcCodeRules"
                  prepend-icon="code"
                  label="MAFC Code"
                  required
                  v-on:keyup.13="subRegister"
                />
                <v-combobox
                  :rules="officeRules"
                  prepend-icon="account_balance"
                  label="Office"
                  required
                  v-on:keyup.13="subRegister"
                />
                <v-combobox
                  :rules="groupRules"
                  prepend-icon="group"
                  label="Group"
                  required
                  v-on:keyup.13="subRegister"
                />
                <!-- <v-text-field
                    ref="username"
                    v-model="user.username"
                    :rules="emailRule"
                    type="email"
                    prepend-icon="email"
                    label="Email"
                    required
                    autofocus
                    v-on:keyup.13="subRegister"/> -->
                <v-layout wrap>
                  <v-flex>
                    <v-btn class="btn_ui" @click="subRegister">
                      Register
                    </v-btn>
                  </v-flex>
                  <v-flex style="text-align: right;">
                    <v-btn right class="btn_ui" @click="btnLoginClick">
                      Login
                    </v-btn>
                  </v-flex>
                </v-layout>
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
import { GoogleSigninButton } from 'components';
import SHA256 from 'sha256';

export default {
  components: {
    GoogleSigninButton
  },
  data: () => ({
    user: {
      username: '',
      password: '',
      firstname: '',
      lastname: '',
      confirmpassword: ''
    },
    loading: false,
    usernameRules: [v => !!v || 'User Name is required'],
    // emailRule: [
    //   v => !!v || 'E-mail is required',
    //   v => /.+@.+/.test(v) || 'E-mail must be valid'
    // ],
    passwordRules: [v => !!v || 'Password is required'],
    confirmpasswordRules: [v => !!v || 'Confirm Password is required'],
    myFullNameRules: [v => !!v || 'My Full Name is required'],
    mafcCodeRules: [v => !!v || 'MAFC Code is required'],
    officeRules: [v => !!v || 'Office is required'],
    groupRules: [v => !!v || 'Group is required']
  }),
  computed: {
    ...mapGetters('register', ['message', 'success', 'signal'])
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

  mounted() {},

  methods: {
    ...mapActions('register', ['registerUser']),
    ...mapActions('global', ['showErrorMsg', 'showSuccessMsg']),

    subRegister() {
      if (!this.user.username) {
        this.$refs.username.focus();
      } else if (!this.user.password) {
        this.$refs.password.focus();
      } else if (!this.user.firstname) {
        this.$refs.firstname.focus();
      } else if (!this.user.lastname) {
        this.$refs.lastname.focus();
      } else if (!this.user.confirmpassword) {
        this.$refs.confirmpassword.focus();
      } else if (!this.validateEmail(this.user.username)) {
        this.$refs.username.focus();
      } else if (this.user.password != this.user.confirmpassword) {
        this.showErrorMsg('Password does not match!');
      } else {
        this.registerUser({
          usrNm: this.user.username,
          usrPwd: SHA256(this.user.password),
          confirmpassword: SHA256(this.user.confirmpassword),
          fstNm: this.user.firstname,
          lstNm: this.user.lastname
        });
      }
    },

    validateEmail(email) {
      let re = /.+@.+/;
      return re.test(String(email).toLowerCase());
    },

    btnLoginClick() {
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.btn_ui {
  background-color: #009688 !important;
  color: white;
  letter-spacing: normal;
  height: 30px !important;
  font-weight: normal !important;
  border-radius: 5px;
  text-transform: none !important;
  font-size: 12px;
  min-width: 0px !important;
  margin-top: 0px;
}
</style>
