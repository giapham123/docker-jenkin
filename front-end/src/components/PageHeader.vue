<template>
  <v-layout row class="align-center layout px-4 pt-4 app--page-header">
    <div class="page-header-left">
      <h3 class="pr-3">{{ title }}</h3>
    </div>
    <v-breadcrumbs divider="-">
      <v-breadcrumbs-item>
        <v-icon larg>mappers.home</v-icon>
      </v-breadcrumbs-item>
      <v-breadcrumbs-item v-for="(item, key) in breadcrumbs" :key="key">
        {{ item }}
      </v-breadcrumbs-item>
    </v-breadcrumbs>
    <v-spacer />
    <div class="page-header-right">
      <v-btn icon> <v-icon class="text--secondary">refresh</v-icon> </v-btn>
    </div>
  </v-layout>
</template>

<script>
import { mapState } from 'vuex';

export default {
  data() {
    return {
      title: ''
    };
  },

  computed: {
    ...mapState('login', ['menus']),

    breadcrumbs() {
      const bc = [];
      this.menus.forEach(item => {
        if (item.items) {
          const child = item.items.find(i => {
            return i.component === this.$route.name;
          });
          if (child) {
            bc.push(item.title);
            bc.push(child.title);
            // this.title = child.title;
          }
        } else {
          if (item.name === this.$route.name) {
            // this.title = item.title;
            bc.push(item.title);
          }
        }
      });
      return bc;
    }
  }
};
</script>
