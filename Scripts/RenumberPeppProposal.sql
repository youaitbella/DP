select ppId as oldId, ppId + 100000 as Id into #id from peppProposal where ppCreated > convert(smalldatetime, '31.03.2019', 104)

set identity_insert peppProposal on

insert into peppProposal (ppId, ppVersion, ppAccountId, ppName, ppCategory, ppStatus, ppCreated, ppDateSealed, ppLastModified, ppCreatedBy, ppLastChangedBy, ppSealedBy, ppInstitute, ppGender, ppTitle, ppFirstName, ppLastName, ppDivision, ppRoleId, ppStreet, ppPostalCode, ppTown, ppPhone, ppFax, ppEmail, ppProblem, ppSolution, ppPepp, ppDiags, ppDiagCodes, ppProcs, ppProcCodes, ppDocumentsOffline, ppAnonymousData, ppChangeMethodDiag, ppChangeMethodProc, ppNote, ppPublication)
select id, ppVersion, ppAccountId, ppName, ppCategory, ppStatus, ppCreated, ppDateSealed, ppLastModified, ppCreatedBy, ppLastChangedBy, ppSealedBy, ppInstitute, ppGender, ppTitle, ppFirstName, ppLastName, ppDivision, ppRoleId, ppStreet, ppPostalCode, ppTown, ppPhone, ppFax, ppEmail, ppProblem, ppSolution, ppPepp, ppDiags, ppDiagCodes, ppProcs, ppProcCodes, ppDocumentsOffline, ppAnonymousData, ppChangeMethodDiag, ppChangeMethodProc, ppNote, ppPublication
from peppProposal 
join #id on ppId = oldId

set identity_insert peppProposal off

update PeppProposalDocument set ppdPeppProposalId = id
from PeppProposalDocument
join #id on ppdPeppProposalId = oldId

update PeppProposalComment set ppcPeppProposalId = id
from PeppProposalComment
join #id on ppcPeppProposalId = oldId

update mapPeppProposalDiagnosis set ppdPeppProposalId = id
from mapPeppProposalDiagnosis
join #id on ppdPeppProposalId = oldId

update mapPeppProposalProcedure set pppPeppProposalId = id
from mapPeppProposalProcedure
join #id on pppPeppProposalId = oldId

delete peppProposal where ppid in (select oldId from #id)

drop table #id

