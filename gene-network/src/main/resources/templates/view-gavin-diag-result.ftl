<#include "molgenis-header.ftl">
<#include "molgenis-footer.ftl">
<#assign css=[]>
<#assign js=["gn-result.js"]>
<@header css js/>

<div class="row" id="gavin-view">
    <div class="col-md-12">
        <div class="panel panel-primary" id="instant-import">
            <div class="panel-heading">
                <h4 class="panel-title">
                    GAVIN annotation job ${jobExecution.filename?html} submitted
                    on ${jobExecution.submissionDate?datetime}
                </h4>
            </div>
            <div class="panel-body">
                <div id="instant-import-alert"></div>

            <#if jobExecution.status == 'RUNNING' || jobExecution.status == 'PENDING'>
            <#-- Job running, show progress -->
                <div id="gavin-job" data-execution-id="${jobExecution.identifier}"></div>
            <p>No results yet. Check back later at <a
                    href="${pageUrl}">${pageUrl}</a>.
            <#else>
            <#-- Job finished -->
                <h4>Input</h4>
                <p>Input file contained:</p>
                <ul>
                    <#if jobExecution.comments?? && jobExecution.comments gt 0 >
                        <li>${jobExecution.comments} comment lines</li></#if>
                    <#if jobExecution.cadds?? && jobExecution.cadds gt 0 >
                        <li>${jobExecution.cadds} valid CADD lines</li></#if>
                    <#if jobExecution.vcfs?? && jobExecution.vcfs gt 0 >
                        <li>${jobExecution.vcfs} valid VCF lines</li></#if>
                    <#if (jobExecution.indels?? && jobExecution.indels gt 0)>
                        <li>${jobExecution.indels} lines contained insertions or deletions without a cadd score.
                            Please pre-score these variants using the <a href="http://cadd.gs.washington.edu/score">CADD
                                service</a>.
                        </li></#if>
                    <#if (jobExecution.errors?? && jobExecution.errors gt 0)>
                        <li>${jobExecution.errors} error lines (could not be parsed)</li></#if>
                    <#if jobExecution.skippeds?? && jobExecution.skippeds gt 0 >
                        <li>${jobExecution.skippeds} skipped lines. Too much input.</li></#if>
                </ul>
                <#if jobExecution.log??>
                    <h4>Execution log</h4>
                    <pre class="pre-scrollable">${jobExecution.log?html}</pre>
                </#if>

                <a class="btn btn-info" type="button" href="${result}">Show results!</a>
            </#if>
            </div>
        </div>
    </div>
</div>

<@footer/>