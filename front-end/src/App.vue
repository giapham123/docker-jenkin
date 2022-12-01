<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <template v-if="$route.meta.requiresAuth">
      <v-app id="inspire" class="app">
        <app-drawer class="app--drawer" />
        <app-toolbar class="app--toolbar" />
        <v-content>
          <!-- Page Header -->
          <page-header v-if="$route.meta.breadcrumb" />
          <div class="page-wrapper"><router-view /></div>
        </v-content>
        <!-- Go to top -->
        <app-fab />
      </v-app>
    </template>
    <template v-else>
      <transition>
        <keep-alive> <router-view /> </keep-alive>
      </transition>
    </template>
    <v-snackbar
      :timeout="5000"
      :color="snackbar.color"
      :multi-line="true"
      v-model="show"
      top
      right
    >
      {{ snackbar.text }}
      <v-btn dark flat icon @click.native="show = false">
        <v-icon>close</v-icon>
      </v-btn>
    </v-snackbar>
  </div>
</template>
<script>
import { mapState, mapGetters, mapActions } from 'vuex';
import { AppDrawer, AppToolbar, AppFab, PageHeader } from 'components';
import AppEvents from 'core/event';

import 'assets/scss/style.scss';
import 'assets/css/custom.css';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  components: {
    AppDrawer,
    AppToolbar,
    AppFab,
    PageHeader,
    Loading
  },

  computed: {
    ...mapGetters('login', ['loading']),
    ...mapState('global', ['snackbar', 'loading']),

    show: {
      get() {
        return this.snackbar.show;
      },
      set() {
        this.resetMsg();
      }
    }
  },

  async created() {
    if (await this.validateToken()) {
      this.$router.push({ path: '/' });
    } else {
      this.$router.push({ path: '/login' });
    }

    AppEvents.forEach(item => {
      this.$on(item.name, item.callback);
    });
  },

  methods: {
    ...mapActions('login', ['validateToken']),
    ...mapActions('global', ['resetMsg', 'toggleDrawer'])
  }
};
</script>

<style lang="scss" scoped>
.setting-fab {
  top: 50% !important;
  right: 0;
  border-radius: 0;
}

.page-wrapper {
  min-height: calc(100vh - 64px - 50px - 81px);
}
</style>
