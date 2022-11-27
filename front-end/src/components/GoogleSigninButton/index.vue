<template>
  <v-btn
    :loading="loading"
    :disabled="loading"
    color="red"
    class="white--text"
    large
    block
    @click="signinWithGoogle"
    @click.native="loader = 'loading'"
  >
    <i class="fab fa-google fa-lg" /> {{ $t('button.connectWithGoogle') }}
  </v-btn>
</template>

<script>
const scopes = [
  'https://www.googleapis.com/auth/plus.me',
  'https://www.googleapis.com/auth/plus.profile.language.read',
  'https://www.googleapis.com/auth/plus.profile.agerange.read',
  'https://www.googleapis.com/auth/userinfo.email',
  'https://www.googleapis.com/auth/userinfo.profile'
];

export default {
  data() {
    return {
      loading: false,
      googleUser: {}
    };
  },

  methods: {
    async signinWithGoogle() {
      this.loading = true;
      try {
        const gapi = window.gapi;
        if (!gapi) {
          // toastr.error('Internet error, please try again.');
          return;
        }

        if (!gapi.auth2) {
          await new Promise(resolve => gapi.load('auth2', resolve));
        }

        const config = {
          client_id: process.env.VUE_APP_GOOGLE_CLIENT_ID,
          scope: scopes.join(' ')
        };

        const googleAuth = await gapi.auth2.init(config);

        if (googleAuth.isSignedIn.get()) {
          await googleAuth.currentUser.get().disconnect();
        }

        const signedIn = await googleAuth.signIn();
        const authRes = signedIn.getAuthResponse();
        this.$emit('success', authRes);
      } catch (e) {
        const { error } = e;
        if (
          [
            'popup_closed_by_user',
            'access_denied',
            'immediate_failed'
          ].includes(error)
        )
          return;
        this.$emit('error', e);
      }
    }
  }
};
</script>

<style lang="stylus" scoped>
button {
  i {
    margin-right: 5px;
  }
}
</style>
