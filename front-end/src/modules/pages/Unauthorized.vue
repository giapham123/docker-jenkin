<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      <div class="mr-3 hidden-sm-and-down">
        <img src="/static/error/401.svg" alt="" />
      </div>
      <div class="text-md-center">
        <h1>401</h1>
        <h2 class="my-3 headline ">
          Sorry, you don't have permission on page.
        </h2>
        <div>
          <v-btn class="white--text" color="#00695c" @click="goHome">
            Go Home
          </v-btn>
        </div>
      </div>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  methods: {
    ...mapActions('login', ['logout']),
    goHome() {
      //  this.$router.push({ path: '/' });

      this.logout().then(() => this.$router.push({ path: '/login' }));
    }
  }
};
</script>
<style scoped lang="css">
h1 {
  font-size: 150px;
  line-height: 150px;
  font-weight: 700;
  color: #252932;
  text-shadow: rgba(61, 61, 61, 0.3) 1px 1px, rgba(61, 61, 61, 0.2) 2px 2px,
    rgba(61, 61, 61, 0.3) 3px 3px;
}
</style>
