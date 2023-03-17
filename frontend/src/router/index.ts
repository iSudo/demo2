import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Doctors from "../views/Doctors.vue";
import MedicalTeams from "../views/MedicalTeams.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/doctors",
    name: "Doctors",
    component: Doctors
  },
  {
    path: "/teams",
    name: "Medical Teams",
    component: MedicalTeams
  }
];

const router = new VueRouter({
  routes
});

export default router;
