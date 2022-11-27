<template>
  <v-data-table
    :headers="headers"
    :items="userList"
    hide-actions
    class="elevation-1"
  >
    <template slot="items" slot-scope="props">
      <td>{{ props.item.fstNm }}</td>
      <td>{{ props.item.lstNm }}</td>
      <td>{{ props.item.usrNm }}</td>
    </template>
  </v-data-table>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  data: () => ({
    headers: [
      {
        text: 'First Name',
        align: 'left',
        sortable: false,
        value: 'fstNm'
      },
      {
        text: 'Last Name',
        align: 'left',
        sortable: false,
        value: 'lstNm'
      },
      {
        text: 'Email',
        align: 'left',
        sortable: false,
        value: 'usrNm'
      }
    ]
  }),
  computed: {
    ...mapGetters('users', ['userList'])
  },

  watch: {},

  mounted() {
    this.getAllUsers();
  },

  methods: {
    ...mapActions('users', ['getAllUser']),

    getAllUsers() {
      this.getAllUser();
    }
  }
};
</script>

<style scoped></style>
