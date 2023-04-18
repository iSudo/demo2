<template>
  <v-container id="teams">
    <v-toolbar-title class="grey--text text--darken-4"
    >Medical Teams
    </v-toolbar-title
    >

    <div v-if="!props.id">
      <v-text-field
          placeholder="Type team name..."
          label="Search"
          v-model="props.search"
      ></v-text-field>

      <v-data-table
          v-if="!props.id"
          :headers="props.headers"
          :items="props.teams"
          :search="props.search"
          class="elevation-1"
      >
        <template #item.actions="{ item }">
          <v-icon small @click="onTeamClick(item)">
            mdi-pencil
          </v-icon>
        </template>
      </v-data-table>
    </div>

    <div v-if="props.id > 0">
      <p class="subtitle-2 text-center">
        Choose {{ props.teams?.find(t => t.id === props.id)?.name }} team doctors:
      </p>

      <v-row justify="space-around">
        <v-checkbox
            class="mx-6"
            v-model="props.checkedDoctors"
            :value="doc.id"
            v-for="doc in props.doctors"
            :key="doc.id"
            :label="doc.firstName + ' ' + doc.lastName"
            @change="onDoctorChange(doc.id)"
        />
      </v-row>

      <v-btn outlined color="primary" @click="backToList">Back</v-btn>
    </div>
  </v-container>
</template>

<script setup lang="ts">
import axiosInstance from '@/axios'
import type {AxiosResponse} from "axios"
import { VDataTable } from 'vuetify/labs/VDataTable'
import type {Doctor} from "@/model/Doctor"
import {onMounted, reactive} from "vue"

interface Team {
  id: number
  name: string
}

interface Data {
  id: number
  search: string
  checkedDoctors: number[]
  headers: any[]
  doctors: Doctor[]
  teams: Team[]
  teamsResponse: Team[]
}

const props = reactive<Data>({
  id: 0,
  search: "",
  teams: [],
  doctors: [],
  checkedDoctors: [],
  headers: [
    {
      key: "id",
      title: "ID",
      filterable: false
    },
    {
      key: "name",
      title: "Team name"
    },
    {
      key: "actions",
      title: "Edit",
      sortable: false
    }
  ],
  teamsResponse: []
})

onMounted(async() => {
  axiosInstance.get("/teams/list").then(response => {
    props.teamsResponse = response.data;
    props.teams = props.teamsResponse;
  });
  axiosInstance.get("/doctors/list").then(response => {
    props.doctors = response.data;
  });
})

const onTeamClick = (item: any) => {
  item = item.raw as Team
  props.id = item.id;
  props.checkedDoctors = props.doctors
      .filter(d => d.medicalTeamId === item.id)
      .map(d => d.id);
}

const backToList = () => {
  props.id = 0;
}
const onDoctorChange = (docId: number) => {
  const doctor = props.doctors.find(d => d.id === docId) as Doctor;
  // backend API does not accept null, it needs empty string for some reason
  doctor.medicalTeamId = (props.checkedDoctors.includes(docId) ? props.id : "" as any);
  axiosInstance.post(
      "/doctors/addOrUpdate",
      new URLSearchParams(doctor as any).toString()
  ).then(response => {
    props.doctors = props.doctors.map(d => {
      if (d.id !== docId) {
        return d;
      }

      return response.data;
    });
  });

  props.checkedDoctors = props.doctors
      .filter(d => d.medicalTeamId === props.id)
      .map(d => d.id);
}

</script>
