<template>
  <v-toolbar color="#00695c" class="white--text" fixed dark app>
    <v-toolbar-title class="ml-0 pl-3">
      <v-toolbar-side-icon @click.stop="handleDrawerToggle" />
    </v-toolbar-title>
    <!-- <v-text-field flat solo-inverted prepend-inner-icon="search" label="Search" class="hidden-sm-and-down" />-->
    <v-spacer />
    <v-btn icon @click="handleFullScreen()">
      <v-icon>fullscreen</v-icon>
    </v-btn>
    <!--<v-icon  @click="handleLogoutClick" >account_circle</v-icon>-->

    <profile :show="is_default_password" @close="handleClosePopup" />
    <v-menu transition="slide-y-transition" bottom left>
      <v-icon slot="activator" icon flat>settings</v-icon>

      <v-list class="pa-0">
        <v-list-tile
          v-for="(item, index) in items"
          :to="!item.href ? { name: item.name } : null"
          :href="item.href"
          :disabled="item.disabled"
          :target="item.target"
          :key="index"
          ripple="ripple"
          rel="noopener"
          @click="item.click"
        >
          <v-list-tile-action v-if="item.icon">
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ item.title }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-menu>
  </v-toolbar>
</template>
<script>
import { mapActions, mapState } from 'vuex';
import Util from 'core/util';
import profile from 'modules/login/popup_Profile';

export default {
  name: 'app-toolbar',
  components: {
    profile
  },
  data() {
    return {
      items: [
        {
          icon: 'account_circle',
          href: 'javascript:void(0)',
          title: 'Profile',
          click: () => {
            this.needChangePassword(true);
          }
        },
        {
          icon: 'fullscreen_exit',
          href: 'javascript:void(0)',
          title: 'Logout',
          click: this.handleLogoutClick
        }
      ]
    };
  },

  computed: {
    ...mapState('global', ['drawerToggled']),
    ...mapState('login', ['is_default_password']),

    toolbarColor() {
      return this.$vuetify.options.extra.mainNav;
    }
  },

  methods: {
    ...mapActions('login', ['logout', 'needChangePassword']),
    ...mapActions('global', ['toggleDrawer']),

    handleDrawerToggle() {
      this.toggleDrawer(!this.drawerToggled);
    },

    handleFullScreen() {
      Util.toggleFullScreen();
    },

    handleLogoutClick() {
      this.logout().then(() => this.$router.push({ path: '/login' }));
    },

    handleClosePopup(param) {
      this.needChangePassword(param);
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
</style>
