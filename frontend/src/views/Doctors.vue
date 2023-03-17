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
                  v-model="search"
                  id="search"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
        <v-data-table
            :headers="headers"
            :items="doctors"
            :search="search"
            class="elevation-1 doctors-table">
          <template v-slot:item.actions="{ item }">
            <v-icon small @click="deleteDoctor(item)">
              mdi-delete
            </v-icon>
          </template>
        </v-data-table>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <v-toolbar-title class="grey--text text--darken-4">
          Add new doctor
        </v-toolbar-title>

        <v-form ref="form" v-model="valid" id="form">
          <v-container>
            <v-row>
              <v-col cols="12" md="4">
                <v-text-field
                    v-model="firstName"
                    :rules="nameRules"
                    label="First name"
                    required
                    id="first-name"
                />
              </v-col>

              <v-col cols="12" md="4">
                <v-text-field
                    v-model="lastName"
                    :rules="nameRules"
                    label="Last name"
                    required
                    id="last-name"
                />
              </v-col>

              <v-col cols="12" md="4">
                <v-date-picker
                    v-model="birthDate"
                    first-day-of-week="1"
                    max="2004-01-01"
                    id="birth-date"
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="4" md="4">
                <v-btn
                    :disabled="!valid || !pickerSelected"
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

<script lang="ts">
import Vue from "vue";
import {DataTableHeader} from "vuetify";
import axios from "@/axios.ts";

export interface Doctor {
  id: number;
  firstName: string;
  lastName: string;
  birthDate: string;
  medicalTeamId?: number;
}

type RuleValidation = (v: string) => string | boolean;

interface Data {
  valid: boolean;
  firstName: string;
  lastName: string;
  birthDate: string;
  nameRules: RuleValidation[];
  doctors: Doctor[];
  doctorsResponse: Doctor[];
  search: string;
  headers: DataTableHeader[];
}

export default Vue.extend({
  data(): Data {
    return {
      valid: true,
      firstName: "",
      lastName: "",
      birthDate: "2000-01-01",
      nameRules: [v => !!v || "Name is required"],
      doctors: [],
      doctorsResponse: [],
      search: "",
      headers: [
        {
          value: "id",
          text: "ID",
          filterable: false
        },
        {
          value: "firstName",
          text: "First name"
        },
        {
          value: "lastName",
          text: "Last name"
        },
        {
          value: "birthDate",
          text: "Birth date",
          filterable: false
        },
        {
          value: "medicalTeamId",
          text: "Medical team ID",
          filterable: false
        },
        {
          value: "actions",
          text: "Delete",
          sortable: false
        }
      ]
    };
  },
  computed: {
    pickerSelected(): boolean {
      return !!this.birthDate;
    }
  },
  methods: {
    async deleteDoctor(item: Doctor) {
      await axios.post(`/doctors/remove?id=${item.id}`);
      this.doctorsResponse = this.doctorsResponse.filter(
          d => d.id !== item.id
      );
      this.doctors = this.doctorsResponse;
    },
    async onCreateDoctor() {
      const doctor = {
        firstName: this.firstName,
        lastName: this.lastName,
        birthDate: this.birthDate
      };
      const newDoctor = (await axios.post("/doctors/addOrUpdate", new URLSearchParams(doctor).toString())).data;
      this.doctorsResponse.push(newDoctor);
      this.doctors = this.doctorsResponse;

      this.firstName = "";
      this.lastName = "";
      this.birthDate = "";
      (this.$refs.form as any).reset();
    }
  },
  async created() {
    this.doctorsResponse = (await axios.get("/doctors/list")).data;
    this.doctors = this.doctorsResponse;
  }
});
</script>
