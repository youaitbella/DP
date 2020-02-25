use DataPortalDev

/*
delete care.proof where prProofRegulationStationId = 0

update care.proof set prProofWardId = 0

delete care.proofWard where pwId > 0
DBCC CHECKIDENT ([care.proofWard], reseed, 1)

*/


--insert into care.ProofWard (pwIk, pwLocationNumber, pwLocationText, pwName)
select prsIk, 0, prsLocationCode, max(replace(prsStationName, '  ', ' '))
from care.ProofRegulationStation
where prsIk > 0
group by prsIk, prsLocationCode, lower(replace(prsStationName, ' ', ''))

--select * from care.ProofWard


/*
-- update existing entries
update care.Proof set prProofWardId = pwId,
                     prProofRegulationStationId = 0,
                     prValidFrom = CONVERT(smalldatetime, '2019-' + CONVERT(varchar, prMonth) + '-01', 102),
                     prValidTo = CONVERT(smalldatetime, EOMONTH(CONVERT(smalldatetime, '2019-' + CONVERT(varchar, prMonth) + '-01', 102))),
                     prDeptNumbers = prsFabNumber,
                     prDeptNames = prsFabName,
                     prSensitiveDomains = sdName,
                     prSignificantSensitiveDomainId = sdId
from care.Proof
        join care.ProofRegulationStation on prProofRegulationStationId = prsId
        join care.ProofWard on prsIk = pwIk and prsLocationCode = pwLocationText and
                               lower(replace(prsStationName, ' ', '')) = lower(replace(pwName, ' ', ''))
        join care.listSensitiveDomain on prsSensitiveAreaId = sdId
where prProofRegulationStationId > 0 and prsYear = 2019
*/