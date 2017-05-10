<template>
  <div class="row">
    <div class="col-md-12">
      <b-alert variant="info" show><h3>Alpha version</h3></b-alert>
      <br>
      <h4>Making it work</h4>
      <hr>
      <ul>
        <li>1: Add diagnostics package to MOLGENIS</li>
        <li>2: Use diagnostics package identifier either via Controller or directly via index.html to add it to the initial state</li>
        <li>3: Import a VCF that is already annotated with gavin</li>
        <li>4: Edit this VCF entitytype and add it to diagnostics package</li>
        <li>5: Import HPO ontology</li>
        <li>6: Add an entity to Matrix metadata and add the matrix file with the GeneNetwork scores</li>
      </ul>
      <br>
      <h4>Working with the platform</h4>
      <hr>
      <p>You should now have the VCF in the "Patients" list on the right. Click on it and start filtering</p>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'home-container'
  }
</script>
