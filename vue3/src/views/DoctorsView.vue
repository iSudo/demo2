<template>
  <v-container id="doctors">
    <v-row>
      <v-col>
        <v-toolbar-title class="grey--text text--darken-4">
          Doctors
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-container id="input-usage" fluid>
          <v-row>
            <v-col cols="12">
              <v-text-field
                  placeholder="Type first or last name..."
                  label="Search"
                  v-model="props.search"
                  id="search"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
        <v-data-table
            :headers="props.headers"
            :items="props.doctors"
            :search="props.search"
            class="elevation-1 doctors-table">
          <template #item.actions="{ item }">
            <v-icon icon="mdi-delete" small @click="deleteDoctor(item)"/>
          </template>
        </v-data-table>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <v-toolbar-title class="grey--text text--darken-4">
          Add new doctor
        </v-toolbar-title>

        <v-form ref="form" v-model="props.valid" id="form">
          <v-container>
            <v-row>
              <v-col cols="12" md="4">
                <v-text-field
                    v-model="props.firstName"
                    :rules="props.nameRules"
                    label="First name"
                    required
                    id="first-name"
                />
              </v-col>

              <v-col cols="12" md="4">
                <v-text-field
                    v-model="props.lastName"
                    :rules="props.nameRules"
                    label="Last name"
                    required
                    id="last-name"
                />
              </v-col>

              <v-col cols="12" md="4">
                <VueDatePicker
                    v-model="props.birthDate"
                    model-type="yyyy-MM-dd"
                    id="birth-date"
                    inline
                    auto-apply
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4" md="4">
                <v-btn
                    :disabled="!props.valid || !pickerSelected"
                    color="success"
                    class="mr-4"
                    @click="onCreateDoctor"
                    id="submit"
                >
                  Submit
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { computed, reactive, ref, onMounted } from 'vue'
import { VDataTable } from 'vuetify/labs/VDataTable'
import axiosInstance from "@/axios"
import type { Doctor } from "@/model/Doctor"

type RuleValidation = (v: string) => string | boolean

interface Data {
  valid: boolean
  firstName: string
  lastName: string
  birthDate: string
  nameRules: RuleValidation[]
  doctors: Doctor[]
  doctorsResponse: Doctor[]
  search: string
  headers: any[]
}

const props = reactive<Data>({
  valid: true,
  firstName: "",
  lastName: "",
  birthDate: "2000-01-01",
  nameRules: [(v: string) => !!v || "Name is required"],
  doctors: [],
  doctorsResponse: [],
  search: "",
  headers: [
    {
      key: "id",
      title: "ID",
      filterable: false
    },
    {
      key: "firstName",
      title: "First name"
    },
    {
      key: "lastName",
      title: "Last name"
    },
    {
      key: "birthDate",
      title: "Birth date",
      filterable: false
    },
    {
      key: "medicalTeamId",
      title: "Medical team ID",
      filterable: false
    },
    {
      key: "actions",
      title: "Delete",
      sortable: false
    }
  ]
})

const pickerSelected = computed(() => !!props.birthDate)
/*
* doctors form ( #form )
* */
const form = ref(null)

onMounted(async() => {
  props.doctorsResponse = (await axiosInstance.get("/doctors/list")).data;
  props.doctors = props.doctorsResponse;
})

const deleteDoctor = async(item: any) => {
  item = item.raw as Doctor
  await axiosInstance.post(`/doctors/remove?id=${item.id}`);
  props.doctorsResponse = props.doctorsResponse.filter(
      (d: Doctor) => d.id !== item.id
  );
  props.doctors = props.doctorsResponse;
}

const onCreateDoctor = async() => {
  const doctor = {
    firstName: props.firstName,
    lastName: props.lastName,
    birthDate: props.birthDate
  };
  const newDoctor = (await axiosInstance.post("/doctors/addOrUpdate", new URLSearchParams(doctor).toString())).data;
  props.doctorsResponse.push(newDoctor);
  props.doctors = props.doctorsResponse;

  props.firstName = "";
  props.lastName = "";
  props.birthDate = "";
  (form.value as any).reset();
}
</script>
