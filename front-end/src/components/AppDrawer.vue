<template>
  <v-navigation-drawer
    id="appDrawer"
    :mini-variant.sync="mini"
    :dark="$vuetify.dark"
    v-model="drawer"
    fixed
    app
    width="260"
  >
    <v-toolbar class="toggle_container" color="#00695c" dark>
      <img
        id="logo"
        src="~assets/images/logo.png"
        height="45"
        alt="Mirae Asset Finance Vietnam"
      />
      <!-- <v-toolbar-title class="ml-0 pl-3">
        <span class="hidden-sm-and-down">Mirae Asset Finance Vietnam</span>
      </v-toolbar-title> -->
      <!-- <div ref="toggle" class="toggle" @click="handleToggle"><v-icon class="white--text">{{toggle_icon}}</v-icon></div>-->
    </v-toolbar>
    <vue-perfect-scrollbar
      :settings="scrollSettings"
      class="drawer-menu--scroll"
    >
      <v-list dense expand>
        <template v-for="(item, i) in accept_menus">
          <!--group with subitems-->
          <v-list-group
            v-if="item.items"
            :key="item.name"
            :group="item.group"
            :prepend-icon="item.icon"
            no-action="no-action"
          >
            <v-list-tile slot="activator" ripple="ripple">
              <v-list-tile-content>
                <v-list-tile-title>{{ item.title }}</v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
            <template v-for="(subItem, i) in item.items">
              <!--sub group-->
              <v-list-group
                v-if="subItem.items"
                :key="subItem.name"
                :group="subItem.group"
                sub-group="sub-group"
              >
                <v-list-tile slot="activator" ripple="ripple">
                  <v-list-tile-content>
                    <v-list-tile-title>{{ subItem.title }}</v-list-tile-title>
                  </v-list-tile-content>
                </v-list-tile>
                <v-list-tile
                  v-for="(grand, i) in subItem.children"
                  :key="i"
                  :to="genChildTarget(item, grand)"
                  :href="grand.href"
                  ripple="ripple"
                >
                  <v-list-tile-content>
                    <v-list-tile-title>{{ grand.title }}</v-list-tile-title>
                  </v-list-tile-content>
                </v-list-tile>
              </v-list-group>
              <!--child item-->
              <v-list-tile
                v-else
                :key="i"
                :to="genChildTarget(item, subItem)"
                :href="subItem.href"
                :disabled="subItem.disabled"
                :target="subItem.target"
                ripple="ripple"
              >
                <v-list-tile-content>
                  <v-list-tile-title>
                    <span>{{ subItem.title }}</span>
                  </v-list-tile-title>
                </v-list-tile-content>
                <!-- <v-circle class="white--text pa-0 circle-pill" v-if="subItem.badge" color="red" disabled="disabled">{{ subItem.badge }}</v-circle> -->
                <v-list-tile-action v-if="subItem.action">
                  <v-icon :class="[subItem.actionClass || 'success--text']">{{
                    subItem.action
                  }}</v-icon>
                </v-list-tile-action>
              </v-list-tile>
            </template>
          </v-list-group>
          <v-subheader v-else-if="item.header" :key="i">{{
            item.header
          }}</v-subheader>
          <v-divider v-else-if="item.divider" :key="i" />
          <!--top-level link-->
          <v-list-tile
            v-else
            :to="!item.href ? { name: item.name } : null"
            :href="item.href"
            :disabled="item.disabled"
            :target="item.target"
            :key="item.name"
            ripple="ripple"
            rel="noopener"
          >
            <v-list-tile-action v-if="item.icon">
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>{{ item.title }}</v-list-tile-title>
            </v-list-tile-content>
            <!-- <v-circle class="white--text pa-0 chip--x-small" v-if="item.badge" :color="item.color || 'primary'" disabled="disabled">{{ item.badge }}</v-circle> -->
            <v-list-tile-action v-if="item.subAction">
              <v-icon class="success--text">{{ item.subAction }}</v-icon>
            </v-list-tile-action>
            <!-- <v-circle class="caption blue lighten-2 white--text mx-0" v-else-if="item.chip" label="label" small="small">{{ item.chip }}</v-circle> -->
          </v-list-tile>
        </template>
      </v-list>
    </vue-perfect-scrollbar>
  </v-navigation-drawer>
</template>
<script>
import { mapGetters, mapState, mapActions } from 'vuex';
import VuePerfectScrollbar from 'vue-perfect-scrollbar';
export default {
  name: 'app-drawer',

  components: {
    VuePerfectScrollbar
  },

  props: {
    expanded: {
      type: Boolean,
      default: true
    }
  },

  data: () => ({
    mini: false,
    scrollSettings: {
      maxScrollbarLength: 160
    },
    accept_menus: [],
    toggle_icon: 'close'
  }),

  computed: {
    ...mapState('login', ['menus']),
    ...mapState('global', {
      toggled: state => state.drawerToggled
    }),
    ...mapGetters('login', ['permissions']),

    computeGroupActive() {
      return true;
    },

    sideToolbarColor() {
      return this.$vuetify.options.extra.sideNav;
    },

    drawer: {
      get() {
        return this.toggled;
      },

      set(val) {
        this.toggleDrawer(val);
      }
    }
  },

  watch: {
    // toggled() {
    //   if (!this.toggled) {
    //     this.toggle_icon = 'arrow_forward_ios';
    //     this.$refs.toggle.className = 'toggle hidden';
    //   } else {
    //     this.toggle_icon = 'close';
    //     this.$refs.toggle.className = 'toggle';
    //   }
    // }
  },

  created() {
    var menus = [];
    let firstPage = null;
    let currentPage = this.$route.name;
    if (this.permissions == null) {
      this.$router.push({ path: '/login' });
      return;
    }

    for (let i = 0; i < this.menus.length; i++) {
      if (
        this.menus[i].header ||
        this.menus[i].divider ||
        this.permissions[this.menus[i].name]
      ) {
        if (firstPage == null && this.menus[i].name) {
          firstPage = this.permissions[this.menus[i].name].url;
        }

        menus.push(this.menus[i].name);
        this.accept_menus.push(this.menus[i]);
      }
    }

    if (!menus.includes(currentPage)) {
      if (firstPage == null) {
        this.$router.push({ path: '/401' });
      } else {
        this.$router.push({ path: firstPage });
      }
    }
  },

  methods: {
    ...mapActions('global', ['toggleDrawer']),

    genChildTarget(item, subItem) {
      if (subItem.href) return;
      if (subItem.component) {
        return {
          name: subItem.component
        };
      }
      return { name: `${item.group}/${subItem.name}` };
    },

    handleToggle() {
      if (this.toggled) {
        this.toggle_icon = 'arrow_forward_ios';
        this.$refs.toggle.className = 'toggle hidden';
      } else {
        this.toggle_icon = 'close';
        this.$refs.toggle.className = 'toggle';
      }
      this.toggleDrawer(!this.toggled);
    }
  }
};
</script>

<style lang="stylus">
// @import '../../node_modules/vuetify/src/stylus/settings/_elevations.styl';
#appDrawer {
  overflow: visible;

  .drawer-menu--scroll {
    height: calc(100vh - 48px);
    overflow: auto;
  }
}

#logo {
    filter: brightness(0) invert(1);
    -webkit-filter: brightness(0) invert(1);
    margin: 0 auto;
}
</style>
