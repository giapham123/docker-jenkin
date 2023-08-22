<template>
  <v-content class="home-app-wrapper">
    <v-container fluid fill-height>
      <v-layout align-center justify-center>
        <v-flex xs12 sm8 md4 lg4>
          <v-card class="elevation-12">
            <v-toolbar dark color="#00695c">
              <v-toolbar-title class="display-1">
                ACCOUNTING MODULE
              </v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <v-form ref="form" lazy-validation>
                <v-text-field
                  ref="username"
                  v-model="user.username"
                  :rules="usernameRules"
                  prepend-icon="account_box"
                  label="User Name"
                  required
                  autofocus
                  v-on:keyup.13="subLoginByLDap"
                />
                <v-text-field
                  ref="password"
                  v-model="user.password"
                  :rules="passwordRules"
                  type="password"
                  label="Password"
                  prepend-icon="lock"
                  required
                  @keypress.enter="subLoginByLDap"
                />
                <v-alert
                  :value="!hasLoadedOnce"
                  color="error"
                  icon="warning"
                  outline
                >
                  {{ message }}
                </v-alert>
                <v-btn
                  :loading="loading"
                  :disabled="loading"
                  color="rgb(0, 150, 136)"
                  class="white--text"
                  large
                  block
                  @click="subLogin"
                >
                  Login
                </v-btn>
                <!-- <v-btn
                  :loading="loading"
                  :disabled="loading"
                  color="rgb(0, 150, 136)"
                  class="white--text"
                  large
                  block
                  @click="subLoginByLDap"
                >
                  Login
                </v-btn> -->
                <!--<v-layout wrap>
                    <v-flex>
                      <v-btn flat color="#00695c" @click="subRegister">
                        Register
                      </v-btn>
                    </v-flex>
                    <v-flex style="text-align: right;">
                      <v-btn right flat color="#00695c" @click="subForgotPass">
                        Forgot Password
                      </v-btn>
                    </v-flex>
                  </v-layout>-->
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
import Service from 'core/service';
import JSEncrypt from 'jsencrypt'
// import { ACCESS_TOKEN_KEY, EXPIRES_AT } from 'core/constants';
// import Service from 'core/service';

export default {
  components: {},
  data: () => ({
    user: {
      username: '',
      password: ''
    },
    loading: false,
    /* usernameRules: [
      v => !!v || 'E-mail is required',
      v => /.+@.+/.test(v) || 'E-mail must be valid'
    ],*/
    passwordRules: [v => !!v || 'Password is required'],
    usernameRules: [v => !!v || 'User Name is required']
  }),

  computed: {
    ...mapGetters('login', ['hasLoadedOnce', 'message'])
  },

  async created() {
    if (await this.validateToken()) {
      this.$router.push('/');
    } else {
      this.logout();
    }
  },

  mounted() {
    Service.interceptors({
      request: request => {
        return request;
      }
    });
  },

  methods: {
    ...mapActions('login', ['login', 'validateToken', 'logout','getKey']),

    async subLogin() {
      this.loading = true;
      if (!this.user.username) {
        this.$refs.username.focus();
      } else if (!this.user.password) {
        this.$refs.password.focus();
      } else {
        await this.login({
          username: this.user.username,
          password: SHA256(this.user.password),
          isLdap: 0
        });
      }
      this.loading = false;
    },
    async subLoginByLDap() {
      this.loading = true;
      var reKey = await this.getKey();
      let publicKey = reKey.data
      let RSAEncrypt = new JSEncrypt();
      RSAEncrypt.setPublicKey(publicKey);
      let encryptedPass = RSAEncrypt.encrypt(this.user.password);
      if (!this.user.username) {
        this.$refs.username.focus();
      } else if (!this.user.password) {
        this.$refs.password.focus();
      } else {
        await this.login({
          username: this.user.username,
          password: encryptedPass,
          isLdap: 1
        });
      }
      this.loading = false;
    },

    subRegister() {
      this.$router.push('/register');
    },

    validateEmail(email) {
      let re = /.+@.+/;
      return re.test(String(email).toLowerCase());
    },

    subForgotPass() {
      this.$router.push('/forgot-password');
    }
  }
};
</script>

<style scoped></style>
