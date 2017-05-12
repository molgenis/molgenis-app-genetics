export const CREATE_ALERT = '__CREATE_ALERT__'
export const REMOVE_ALERT = '__REMOVE_ALERT__'
export const UPDATE_JOB = '__UPDATE_JOB__'
export const UPDATE_JOB_HREF = '__UPDATE_JOB_HREF__'
export const SET_TOKEN = '__SET_TOKEN__'
export const SET_PATIENT_TABLES = '__SET_PATIENT_TABLES__'
export const SET_PHENOTYPE_FILTERS = '__SET_PHENOTYPE_FILTERS__'
export const SET_VARIANTS = '__SET_VARIANTS__'
export const TOGGLE_ACTIVE_PHENOTYPE_FILTERS = '__TOGGLE_ACTIVE_PHENOTYPE_FILTERS__'
export const SET_GENE_NETWORK_SCORES = '__SET_GENE_NETWORK_SCORES__'
export const REMOVE_GENE_NETWORK_SCORES = '__REMOVE_GENE_NETWORK_SCORES__'
export const UPDATE_VARIANT_SCORES = '__UPDATE_VARIANT_SCORES__'

export default {
  /**
   * Create an active alert with a given message and type
   *
   * @param state state of the application
   * @param alert alert object containing 1. message and 2. type
   */
  [CREATE_ALERT] (state, alert) {
    state.showAlert = true
    state.alert = alert
  },
  /**
   * Remove an active alert and set message and type back to null
   *
   * @param state state of the application
   */
  [REMOVE_ALERT] (state) {
    state.showAlert = false
    state.alert.message = null
    state.alert.type = null
  },
  /**
   * Update the job currently registered with the state of the application
   *
   * @param state state of the application
   * @param job the job to update the state with
   */
  [UPDATE_JOB] (state, job) {
    state.job = job
  },
  /**
   * Update job href currently registered with the state of the application
   *
   * @param state state of the application
   * @param jobHref the jobHref to update the state with
   */
  [UPDATE_JOB_HREF] (state, jobHref) {
    state.jobHref = jobHref
  },
  /**
   * Set the token for the entire application and all subsequent API calls
   *
   * @param state state of the application
   * @param token the token used to set the token in the state
   */
  [SET_TOKEN] (state, token) {
    state.token = token
  },
  /**
   * Set the list of EntityTypes origination from the diagnostics package
   *
   * @param state state of the application
   * @param patients list of EntityTypes used to set the list of patients in the state
   */
  [SET_PATIENT_TABLES] (state, patients) {
    state.patients = patients
  },
  /**
   * Sets the list of currently selected phenotypes in the format
   * {id:..., label:..., isActive:...}
   *
   * @param state state of the application
   * @param phenotypeFilters list of selected phenotypes, may be empty
   */
  [SET_PHENOTYPE_FILTERS] (state, phenotypeFilters) {
    state.phenotypeFilters = phenotypeFilters.slice()
  },
  /**
   * Toggle active state of a phenotype filter based on a phenotype ID
   *
   * @param state state of the application
   * @param phenotypeId the ID of the phenotype filter that was toggled on or off
   */
  [TOGGLE_ACTIVE_PHENOTYPE_FILTERS] (state, phenotypeId) {
    let phenotypeFilter = state.phenotypeFilters.find(function (phenotypeFilter) {
      return phenotypeFilter.id === phenotypeId
    })
    phenotypeFilter.isActive = !phenotypeFilter.isActive
  },
  /**
   * Store all the variants in the state for the selected patient
   *
   * @param state state of the application
   * @param variants list of variant objects retrieved from the database
   */
  [SET_VARIANTS] (state, variants) {
    variants.map(function (variantEntity) {
      const split = variantEntity.EFFECT.split('|')
      variantEntity.cDNA = split[9]
      variantEntity.pChange = split[10]
      variantEntity.gavinReason = split[18]
      return variantEntity
    })
    state.variants = variants
  },
  /**
   * Update variant entity objects in the state with a GeneNetwork score
   * Retrieves scores for all phenotypeFilters from state
   *
   * @param state state of the application
   */
  [UPDATE_VARIANT_SCORES] (state) {
    state.variants = state.variants.map(function (variantEntity) {
      let totalSum = 0
      state.phenotypeFilters.map(function (phenotypeFilter) {
        if (phenotypeFilter.isActive) {
          const phenotypeId = phenotypeFilter.id
          const scoreMap = state.geneNetworkScores[phenotypeId]
          if (scoreMap !== undefined) {
            totalSum = totalSum + scoreMap[variantEntity.Gene_Name]
          }
        }
      })
      variantEntity.totalScore = totalSum
      return variantEntity
    })
  },
  /**
   * Use reduce function to produce {Phenotype1: {gene1: score, gene2: score...}, Phenotype2: {...}}
   *
   * @param state state of the application
   * @param scores list of scores returned by the server
   */
  [SET_GENE_NETWORK_SCORES] (state, scores) {
    const phenotypeId = scores[0].column
    state.geneNetworkScores[phenotypeId] = scores.reduce(function (acc, score) {
      return {...acc, [score.row]: score.value}
    }, {})
  },
  /**
   * Remove geneNetwork scores for a specific phenotypeId. Updates scores in the Variant table
   *
   * @param state state of the application
   * @param phenotypeId Id of the filter that was removed
   */
  [REMOVE_GENE_NETWORK_SCORES] (state, removedPhenotypeFilter) {
    delete state.geneNetworkScores[removedPhenotypeFilter.id]
  }
}
