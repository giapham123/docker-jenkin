<template>
  <v-dialog v-model="dialog" fullscreen>
    <v-card
      style=" background-color: #FFFFFF; height: 100%"
      class="overflow_scroll_vertical"
    >
      <v-form v-model="valid">
        <v-container class="home-app-wrapper" grid-list-md fluid>
          <v-card class="no_shaddow">
            <v-card-text style="width: 100%">
              <v-card style="border: 1px solid #92D2CD;">
                <v-card-title
                  style="background-color: #009688; padding-top:4px; padding-bottom: 4px; color: white"
                >
                  Check partner blacklist customer
                </v-card-title>
                <v-card>
                  <v-card-title>
                    <v-text-field
                      v-model="search"
                      append-icon="search"
                      label="Search partner"
                      single-line
                      hide-details
                    />
                  </v-card-title>
                  <v-data-table
                    :headers="headers"
                    :items="blackList"
                    :search="search"
                    :rows-per-page-items="[5, 10]"
                  >
                    <template slot="items" slot-scope="props">
                      <td>{{ props.item.id }}</td>
                      <td>{{ props.item.type }}</td>
                      <td>{{ props.item.companyName }}</td>
                      <td>{{ props.item.companyTaxCode }}</td>
                      <td>{{ props.item.companyAddress }}</td>
                    </template>
                    <v-alert
                      slot="no-results"
                      :value="true"
                      color="error"
                      icon="warning"
                    >
                      Your search for "{{ search }}" found no results.
                    </v-alert>
                  </v-data-table>
                </v-card>
              </v-card>
            </v-card-text>
            <v-card-actions>
              <v-spacer />
              <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnCancelClick"
              >
                {{ $t('button.cancel') }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-container>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
export default {
  props: {
    show: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      valid: true,
      dialog: false,
      search: '',
      headers: [
        { text: 'No.', value: 'id' },
        { text: 'Type', value: 'type' },
        { text: 'Company name', value: 'companyName' },
        { text: 'Tax code', value: 'companyTaxCode' },
        { text: 'Address', value: 'companyAddress' }
      ]
    };
  },

  computed: {
    ...mapGetters('dashboard', ['blackList'])
  },

  watch: {
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
        this.search = '';
      } else {
        this.getAll();
      }
    }
  },

  created() {
    this.dialog = this.show;
  },

  mounted() {},

  methods: {
    ...mapActions('dashboard', ['getAllBlackListPartner']),
    emitShownState(flag) {
      this.$emit('shown', flag);
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
    },

    getAll() {
      this.getAllBlackListPartner();
    }
  }
};
</script>

<style lang="scss" scoped>
.file-select > .select-button {
  padding-top: 1rem;
  padding-bottom: 1rem;
  padding-right: 3%;
  padding-left: 3%;
  color: white;
  background-color: #2ea169;

  border-radius: 0.3rem;

  text-align: center;
  font-weight: bold;
}

.file-select > input[type='file'] {
  display: none;
}
.container.grid-list-md .layout .inputPadding {
  padding-right: 50px;
}
</style>
