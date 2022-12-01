<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card-title style="color:#02786B" class="display-1">
        Group Feature Manager
      </v-card-title>
      <v-container grid-list-md fluid>
        <v-layout row wrap class="block" justify-center>
          <v-flex md12>
            <v-card>
              <v-card-text>
                <v-layout row wrap>
                  <v-flex md12>
                    <v-select
                      ref="groupId"
                      v-model="groupFeatureInfo.groupId"
                      :items="listGroup"
                      box
                      label="Group"
                    />
                  </v-flex>
                </v-layout>
                <v-layout row wrap>
                  <v-flex md12>
                    <v-select
                      ref="featureId"
                      v-model="groupFeatureInfo.featureId"
                      :items="listFeature"
                      box
                      label="Feature"
                  /></v-flex>
                </v-layout>
                <v-layout row wrap justify-center>
                  <v-flex xs12 md6>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="handleBtnSearchClick"
                    >
                      Search
                    </v-btn>
                  </v-flex>
                  <v-flex xs12 md6>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="handleBtnAddClick"
                    >
                      Register
                    </v-btn>
                  </v-flex>
                </v-layout>
                <v-layout row wrap />
              </v-card-text>
            </v-card>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex md12>
            <v-data-table
              :headers="headers"
              :items="listGroupFeatureSearch"
              class="elevation-1"
            >
              <template slot="items" slot-scope="props">
                <tr>
                  <td class="text-xs-left">{{ props.item.id }}</td>
                  <td class="text-xs-left">{{ props.item.groupId }}</td>
                  <td class="text-xs-left">{{ props.item.groupName }}</td>
                  <td class="text-xs-left">{{ props.item.featureId }}</td>
                  <td class="text-xs-left">{{ props.item.featureName }}</td>
                  <td class="text-xs-center">
                    <v-btn
                      icon
                      class="vbutton_icon"
                      @click="handleDeleteClick(props.item)"
                    >
                      <v-icon>delete_forever</v-icon>
                    </v-btn>
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  data() {
    return {
      groupFeatureInfo: {
        id: '',
        groupId: '',
        groupName: '',
        featureId: '',
        featureName: '',
        hiddenFieldId: ''
      },
      listGroup: [],
      listFeature: [],
      listGroupFeatureSearch: [],
      headers: [
        { text: 'Id', sortable: true, value: 'id' },
        { text: 'GroupId', sortable: true, value: 'groupId' },
        { text: 'GroupName', sortable: true, value: 'groupName' },
        { text: 'FeatureId', sortable: true, value: 'featureId' },
        { text: 'FeatureName', sortable: true, value: 'featureName' }
      ],
      btnDisabled: true
    };
  },

  created() {
    this.loadGroup();
    this.loadFeature();
  },

  methods: {
    ...mapActions('groupfeature', [
      'getAllGroup',
      'getAllFeature',
      'loadGroupFeature',
      'insGroupFeature',
      'delGroupFeature'
    ]),

    handleBtnSearchClick() {
      this.loadGroupFeatureInfo();
    },

    handleBtnAddClick() {
      this.addGroupFeature();
    },

    handleDeleteClick(chooseItem) {
      this.delGroupFeatureInfo(chooseItem);
    },

    async loadGroup() {
      const result = await this.getAllGroup();
      if (result.data != null) {
        var listGroupTemp = [];
        listGroupTemp.push({
          text: 'All',
          value: null
        });
        result.data.forEach(element => {
          listGroupTemp.push({
            text: element.name,
            value: element.codeId
          });
        });
        this.listGroup = listGroupTemp;
      } else {
        this.listGroup = [];
      }
    },

    async loadFeature() {
      const result = await this.getAllFeature();
      if (result.data != null) {
        var listFeatureTemp = [];
        listFeatureTemp.push({
          text: 'All',
          value: null
        });
        result.data.forEach(element => {
          listFeatureTemp.push({
            text: element.name,
            value: element.codeId
          });
        });
        this.listFeature = listFeatureTemp;
      } else {
        this.listFeature = [];
      }
    },

    async addGroupFeature() {
      if (
        this.groupFeatureInfo.groupId == null ||
        this.groupFeatureInfo.groupId == ''
      ) {
        this.$refs.groupId.focus();
      } else if (
        this.groupFeatureInfo.featureId == null ||
        this.groupFeatureInfo.featureId == ''
      ) {
        this.$refs.featureId.focus();
      } else {
        const result = await this.insGroupFeature(this.groupFeatureInfo);
        if (result.success == true) {
          if (result.data != null && result.data != '') {
            this.$message.error(
              'This feature has already existed in this group.'
            );
          } else {
            this.$message.success('Register successfull.');
          }
        } else {
          this.$message.error('Error. Please try again.');
        }
        this.groupFeatureInfo.featureId = '';
        this.loadGroupFeatureInfo();
      }
    },

    async delGroupFeatureInfo(item) {
      this.groupFeatureInfo.id = item.id;
      const result = await this.delGroupFeature(this.groupFeatureInfo);
      if (result.success == true) {
        this.$message.success('Delete feature successfull.');
      } else {
        this.$message.error('Error. Please try again.');
      }
      this.loadGroupFeatureInfo();
    },

    async loadGroupFeatureInfo() {
      const result = await this.loadGroupFeature(this.groupFeatureInfo);
      if (result.data != null && result.data != '') {
        this.listGroupFeatureSearch = result.data;
      } else {
        this.listGroupFeatureSearch = [];
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.vbutton {
  background-color: #009688 !important;
  color: #ffffff;
  width: 300px;
}
.vbutton_icon {
  background-color: #009688 !important;
}
</style>
