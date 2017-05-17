<template>
  <div class="row">
    <div class="col-md-12">
      <h4>Patients</h4>
      <hr>
      View variants for each patient (VCF) by clicking on a patient link on the left.
      <br><br>
      When viewing a patient, you can look for HPO phenotypes. Phenotype selection will sort variants based on GeneNetwork score.
      <br><br>
      To import a patient (VCF), you can either go to the <a href="/menu/main/diagnostics">Diagnostic annotator</a>, or import a VCF that was already annotated with GAVIN <a href="/upload">here</a>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'patients-help-text'
  }
</script>
