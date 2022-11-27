<template>
  <v-content>
    <v-container fluid fill-height>
      <v-layout align-center justify-center>
        <v-flex xs12 sm8 md4 lg4>
          <v-card class="elevation-12">
            <v-toolbar dark color="primary">
              <v-toolbar-title>Forgot Password</v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <v-form ref="form" lazy-validation onsubmit="return false;">
                <p class="text-sm-left">
                  Enter your email address and we'll sent you a link to reset
                  your password
                </p>
                <v-text-field
                  ref="email"
                  v-model="user.email"
                  :rules="usernameRules"
                  type="email"
                  prepend-icon="email"
                  label="Email"
                  required
                  autofocus
                  v-on:keyup.13="subSentEmail"
                />
                <v-btn
                  color="primary"
                  primary
                  large
                  block
                  @click="subSentEmail"
                >
                  Submit
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

export default {
  components: {},
  data: () => ({
    user: {
      email: ''
    },
    loading: false,
    usernameRules: [
      v => !!v || 'E-mail is required',
      v => /.+@.+/.test(v) || 'E-mail must be valid'
    ]
  }),
  computed: {
    ...mapGetters('forgotpassword', ['message', 'success'])
  },

  watch: {
    success() {
      if (this.success) {
        this.showSuccessMsg(this.message);
      } else {
        this.showErrorMsg(this.message);
      }
    }
  },

  mounted() {},

  methods: {
    ...mapActions('forgotpassword', ['sendForgotPassEmail']),
    ...mapActions('global', ['showErrorMsg', 'showSuccessMsg']),

    subSentEmail() {
      if (!this.user.email) {
        this.$refs.email.focus();
      } else if (!this.validateEmail(this.user.email)) {
        this.$refs.email.focus();
      } else {
        this.sendForgotPassEmail({
          email: this.user.email
        });
      }
    },

    validateEmail(email) {
      let re = /.+@.+/;
      return re.test(String(email).toLowerCase());
    }
  }
};
</script>

<style scoped></style>
