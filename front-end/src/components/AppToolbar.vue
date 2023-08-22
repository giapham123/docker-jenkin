<template>
  <v-toolbar color="#00695c" class="white--text" fixed dark app>
    <v-toolbar-title class="ml-0 pl-3">
      <v-toolbar-side-icon @click.stop="handleDrawerToggle" />
    </v-toolbar-title>
    <v-spacer />
    <profile :show="is_default_password" @close="handleClosePopup" />
    <template>
      <v-toolbar-items class="mr-3">
        <v-btn flat class="personal" @click="handleClosePopup(true)">
          <v-icon left>people</v-icon>
          {{ upper(me.account_id) }}
        </v-btn>
      </v-toolbar-items>
      <v-btn icon @click="handleLogoutClick()">
        <v-icon>exit_to_app</v-icon>
      </v-btn>
      <div v-show="false">{{ stateCallServerShow }}</div>
    </template>
  </v-toolbar>
</template>
<script>
import { mapActions, mapState, mapGetters } from 'vuex';
import _ from 'lodash';
import profile from 'modules/login/popup_Profile';

export default {
  name: 'app-toolbar',
  components: {
    profile
  },
  data() {
    return {};
  },

  computed: {
    ...mapState('global', ['drawerToggled']),
    ...mapState('login', ['is_default_password']),
    ...mapGetters('login', ['me', 'isCallServer']),

    toolbarColor() {
      return this.$vuetify.options.extra.mainNav;
    },
    async stateCallServerShow() {
      var stateCallServer = this.isCallServer;
      if (stateCallServer) {
        await this.updateTime({
          accountId: localStorage.getItem('userId'),
          timeKeepAccount: 0
        });
      }
      this.updateStateCallServer(false);
      return stateCallServer;
    }
  },

  methods: {
    ...mapActions('login', [
      'logout',
      'needChangePassword',
      'removeUserLogin',
      'updateStateCallServer',
      'updateTime'
    ]),
    ...mapActions('global', ['toggleDrawer']),

    handleDrawerToggle() {
      this.toggleDrawer(!this.drawerToggled);
    },

    async handleLogoutClick() {
      await this.removeUserLogin({ username: this.me.account_id });
      this.logout().then(() => this.$router.push({ path: '/login' }));
      localStorage.removeItem('userId');
    },

    handleClosePopup(param) {
      this.needChangePassword(param);
    },

    upper(val) {
      return _.toUpper(val);
    }
  }
};
</script>

<style lang="scss" scoped>
.v-toolbar {
  .v-toolbar__content {
    padding-left: 0;
  }
}
.v-text-field {
  .v-input__slot {
    margin: 0 !important;
  }
}

.personal {
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  padding: 0 20px;
  cursor: pointer;
  margin: 0 5px 0 0;
}

.personal:hover {
  background-color: rgba(0, 0, 0, 0.4);
}

.personal > img {
  float: left;
  height: 26px;
  margin: 12px auto;
}

.personal > div {
  float: right;
  margin: 14px 0 14px 14px;
  font-weight: 600;
}
</style>
