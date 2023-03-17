<template>
  <v-container id="teams">
    <v-toolbar-title class="grey--text text--darken-4"
    >Medical Teams
    </v-toolbar-title
    >

    <div v-if="!id">
      <v-text-field
          placeholder="Type team name..."
          label="Search"
          v-model="search"
      ></v-text-field>

      <v-data-table
          v-if="!id"
          :headers="headers"
          :items="teams"
          :search="search"
          class="elevation-1"
          @click:row="onTeamClick"
      >
        <template v-slot:item.actions="{ item }">
          <v-icon small @click="onTeamClick(item)">
            mdi-pencil
          </v-icon>
        </template>
      </v-data-table>
    </div>

    <div v-if="id">
      <p class="subtitle-2 text-center">
        Choose {{ teams.find(t => t.id === id).name }} team doctors:
      </p>

      <v-row justify="space-around">
        <v-checkbox
            class="mx-6"
            v-model="checkedDoctors"
            :value="doc.id"
            v-for="doc in doctors"
            :key="doc.id"
            :label="doc.firstName + ' ' + doc.lastName"
            @change="onDoctorChange(doc.id)"
        />
      </v-row>

      <v-btn outlined color="primary" @click="backToList">Back</v-btn>
    </div>
  </v-container>
</template>

<script lang="ts">
import Vue from "vue";
import {DataTableHeader} from "vuetify";
import axios from '@/axios';
import {Doctor} from "@/views/Doctors.vue";

interface Team {
  id: number;
  name: string;
}

interface Data {
  id: number;
  search: string;
  checkedDoctors: number[];
  headers: DataTableHeader[];
  doctors: Doctor[];
  teams: Team[];
  teamsResponse: Team[];
}

export default Vue.extend({
  data(): Data {
    return {
      id: 0,
      search: "",
      teams: [],
      doctors: [],
      checkedDoctors: [],
      headers: [
        {
          value: "id",
          text: "ID",
          filterable: false
        },
        {
          value: "name",
          text: "Team name"
        },
        {
          value: "actions",
          text: "Edit",
          sortable: false
        }
      ],
      teamsResponse: []
    };
  },
  methods: {
    onTeamClick(item: Team) {
      this.id = item.id;

      this.checkedDoctors = this.doctors
          .filter(d => d.medicalTeamId === item.id)
          .map(d => d.id);
    },
    backToList() {
      this.id = 0;
    },
    onDoctorChange(docId: number) {
      const doctor = this.doctors.find(d => d.id === docId) as Doctor;
      // backend API does not accept null, it needs empty string for some reason
      doctor.medicalTeamId = (this.checkedDoctors.includes(docId) ? this.id : "" as any);
      axios.post(
          "/doctors/addOrUpdate",
          new URLSearchParams(doctor as any).toString()
      ).then(response => {
        this.doctors = this.doctors.map(d => {
          if (d.id !== docId) {
            return d;
          }

          return response.data;
        });
      });

      this.checkedDoctors = this.doctors
          .filter(d => d.medicalTeamId === this.id)
          .map(d => d.id);
    }
  },
  created() {
    axios.get("/teams/list").then(response => {
      this.teamsResponse = response.data;
      this.teams = this.teamsResponse;
    });
    axios.get("/doctors/list").then(response => {
      this.doctors = response.data;
    });
  }
});
</script>
