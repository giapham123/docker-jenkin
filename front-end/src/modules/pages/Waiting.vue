<template>
  <div class="waiting-wrapper">
    <v-progress-circular
      :size="100"
      indeterminate
      color="#00695c"
    ></v-progress-circular>
  </div>
</template>
<script>
import __find from 'lodash/find';
import { mapState, mapGetters } from 'vuex';

export default {
  computed: {
    ...mapState('login', ['menus']),
    ...mapGetters('login', ['permissions'])
  },

  created() {
    const route = __find(this.menus, route => this.permissions[route.name]);
    if (route) {
      this.$router.push({ name: route.name });
    } else {
      this.$router.push({ path: '/403' });
    }
  }
};
</script>

<style scoped>
.waiting-wrapper {
  position: fixed;
  top: calc(50% - 50px);
  left: calc(50% - 50px);
}
</style>
