<template>
  <v-navigation-drawer
    id="appDrawer"
    :mini-variant.sync="mini"
    :dark="$vuetify.dark"
    v-model="drawer"
    app
    width="300"
  >
    <v-toolbar class="toggle_container" color="#00695c" dark>
      <img
        id="logo"
        src="~assets/images/logo.png"
        height="55"
        alt="Mirae Asset Finance Vietnam"
      />
    </v-toolbar>
    <v-list dense>
      <div v-for="(item, i) in accept_menus" :key="i">
        <v-list-group
          v-if="item.childs"
          :prepend-icon="item.icon"
          v-model="item.active"
          :active-class="
            active.group == item.group ? 'primary_color' : 'no_active_group'
          "
          no-action
        >
          <v-list-tile slot="activator">
            <v-list-tile-title class="no_select sub_header">{{
              $t(item.text)
            }}</v-list-tile-title>
          </v-list-tile>
          <template v-for="(childItem, j) in item.childs">
            <v-list-tile
              v-if="!childItem.childs"
              :disabled="childItem.disabled"
              :key="j"
              :class="[
                active.tab == childItem.name ? 'active_tab primary_color' : ''
              ]"
              ripple="ripple"
              @click="goTo(childItem.path)"
            >
              <v-list-tile-title class="no_select">
                {{ $t(childItem.text) }}
              </v-list-tile-title>
            </v-list-tile>

            <v-list-group
              v-else
              :key="j"
              :group="childItem.subGroup"
              :active-class="
                active.group == childItem.subGroup
                  ? 'primary_color'
                  : 'no_active_group'
              "
              sub-group
            >
              <v-list-tile slot="activator" avatar>
                <v-list-tile-title class="no_select sub_header">{{
                  $t(childItem.text)
                }}</v-list-tile-title>
              </v-list-tile>
              <template v-for="(childItem2, j) in childItem.childs">
                <v-list-tile
                  v-if="!childItem2.childs"
                  :disabled="childItem2.disabled"
                  :key="j"
                  :class="[
                    active.tab == childItem2.name
                      ? 'active_tab primary_color'
                      : ''
                  ]"
                  ripple="ripple"
                  @click="goTo(childItem2.path)"
                >
                  <v-list-tile-title
                    class="no_select"
                    style="padding-left:20px"
                  >
                    {{ $t(childItem2.text) }}
                  </v-list-tile-title>
                </v-list-tile>
              </template>
            </v-list-group>
          </template>
        </v-list-group>
        <v-list-tile
          v-else
          :class="[active.tab == item.name ? 'active_tab primary_color' : '']"
          ripple="ripple"
          @click="goTo(item.path)"
        >
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title class="no_select sub_header">
              {{ $t(item.text) }}
            </v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </div>
    </v-list>
  </v-navigation-drawer>
</template>
<script>
import { BASE_ROUTES, TERMI_ROUTES, PARENT_MENU } from 'router/routes';
import { mapGetters, mapState, mapActions } from 'vuex';
import VuePerfectScrollbar from 'vue-perfect-scrollbar';
import _ from 'lodash';
import __isEmpty from 'lodash/isEmpty';
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
    toggle_icon: 'close',
    active: {},
    itemDashboard: {}
  }),

  computed: {
    ...mapState('login', ['menus']),
    ...mapState('global', {
      toggled: state => state.drawerToggled
    }),
    ...mapGetters('login', ['permissions']),

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
    },

    permissions_routes() {
      if (!this.permissions || _.isEmpty(this.permissions))
        return [...BASE_ROUTES];
      let result = {};
      for (let property in PARENT_MENU) {
        for (let i = 0; i < PARENT_MENU[property].length; i++) {
          if (!__isEmpty(this.permissions[PARENT_MENU[property][i]])) {
            result[property] = 'PARENT_MENU';
          }
        }
      }
      _.assign(this.permissions, result);
      return [
        ...BASE_ROUTES,
        ...TERMI_ROUTES.filter(
          ({ name }) => !_.isNil(this.permissions[name])
        ).map(route => route)
      ];
    }
  },

  watch: {
    $route(to) {
      this.active = {
        tab: to.name,
        group: to.meta.group
      };
    },

    permissions: {
      handler: 'readListMenuInRoutes',
      immediate: true
    }
  },

  created() {
    if (localStorage.getItem('RE_LOAD_PAGE') == 'true') {
      location.reload();
      localStorage.setItem('RE_LOAD_PAGE', 'false');
    }

    this.active = {
      tab: this.$route.name,
      group: this.$route.meta.group
    };
  },

  methods: {
    ...mapActions('global', ['toggleDrawer']),

    goTo(path) {
      this.$router.push({ path: path });
    },

    readListMenuInRoutes() {
      this.accept_menus = [];
      this.permissions_routes.forEach(route => {
        if (route.meta && route.meta.menu) {
          let item = {
            text: route.meta.title,
            icon: route.meta.icon || 'work',
            path: route.path,
            active: false
          };
          if (!route.meta.group) {
            item.name = route.name;
          } else if (route.meta.group && route.meta.super) {
            let childItems = this.readChildrenMenus(route.meta.group);
            if (childItems.length > 0) {
              var isExistUrl = false;
              childItems.forEach(el => {
                if (el.path == window.location.pathname) {
                  isExistUrl = true;
                }
              });
              if (isExistUrl) {
                item.active = true;
              }
              item.childs = childItems;
              item.group = route.meta.group;
            }
          } else {
            return;
          }
          this.accept_menus.push(item);
        }
      });
    },

    readChildrenMenus(group) {
      return this.permissions_routes
        .filter(({ meta }) => !!meta && !meta.super && group === meta.group)
        .map(route => {
          let childs = [];
          if (route.meta.isGroup) {
            childs = this.readChildrenMenusLevel3(route.meta.subGroup);
            return {
              childs: childs,
              text: route.meta.title,
              name: route.name,
              group: route.meta.group,
              path: route.path,
              icon: route.icon
            };
          }
          return {
            text: route.meta.title,
            name: route.name,
            group: route.meta.group,
            path: route.path,
            icon: route.icon
          };
        });
    },
    readChildrenMenusLevel3(group) {
      return this.permissions_routes
        .filter(({ meta }) => !!meta && !meta.super && group === meta.group)
        .map(route => ({
          text: route.meta.title,
          name: route.name,
          group: route.meta.group,
          path: route.path,
          icon: route.icon
        }));
    }
  }
};
</script>

<style lang="stylus">
// @import '../../node_modules/vuetify/src/stylus/settings/_elevations.styl';
#appDrawer {
  //overflow: visible;

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

<style lang="scss" scoped>
.primary_color {
  color: #007bff !important;
}

.no_active_group i {
  color: var(--v-primary-base) !important;
}

.active_tab {
  background: #e1e1e1;
}

.active_tab i {
  color: #00695c;
}

.no_select {
  user-select: none;
}

.sub_header {
  font-weight: bold;
  font-size: 14px;
  text-align: justify;
}
</style>
