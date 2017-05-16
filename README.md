![Build Status](https://molgenis50.gcc.rug.nl/jenkins/buildStatus/icon?job=molgenis-genetics?raw=true)

# molgenis-genetics
This repository contains modules designed for viewing, filtering, annotating, visualising, and querying genetic data.

Code is built upon 

## Deploying GeneNetwork application
After deploying the molgenis-genetics WAR to a server, follow these steps to get the GeneNetwork application running

1. Import HPO ontology
2. Configure a MatrixMetadata entity with the following files
    - matrix file: GeneNetwork.txt
        Giant matrix containing GeneNetwork scores for HPO - Ensembl Gene combinations
    - rowMappingFile: mart_export.txt
        Translation file between HGNC Gene symbols and Ensembl Gene identifiers
3. Import a VCF via the GAVIN annotation plugin
4. View results

In the gene-network/patients/[id] screen, you can search 

## Configuring GAVIN annotation for VCF files
If you enable the 'diagnostics' plugin in the menu, it will allow you to annotate your VCF files
with GAVIN. You will need the following resources to do this:

1. SnpEff.jar
2. CADD.tsv.gz
3. exac.vcf.gz
4. GAVIN_calibrations_r0.3.xlsx
