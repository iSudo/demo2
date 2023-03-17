<template>
  <v-app>
    <v-content>
      <div id="app">
        <v-tabs>
          <v-tab to="/" class="home">Home</v-tab>
          <v-tab to="/doctors" class="doctors">Doctors</v-tab>
          <v-tab to="/teams" class="teams">Medical Teams</v-tab>
        </v-tabs>
        <router-view/>
      </div>
    </v-content>
  </v-app>
</template>
