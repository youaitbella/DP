use DataPortalDev

/*
delete care.proof where prProofRegulationStationId = 0

delete care.proofWard where pwId > 0
DBCC CHECKIDENT ([care.proofWard], reseed, 1)

*/


--insert into care.ProofWard (pwIk, pwLocationNumber, pwLocationText, pwName)
select prsIk, 0, prsLocationCode, max(replace(prsStationName, '  ', ' '))
from care.ProofRegulationStation
where prsIk > 0
group by prsIk, prsLocationCode, lower(replace(prsStationName, ' ', ''))
--group by prsIk, prsLocationCode, prsStationName

--select * from care.ProofWard

;
with sensitiveRanking(sensitiveDomainId, seq) as (
    select d.pbdSensitiveAreaId, row_number() over (order by d.pbdPpug, n.pbdPpug, d.pbdPart, n.pbdPart) as seq
    from DataPortal.care.PpugBaseData d
             join DataPortal.care.PpugBaseData n on d.pbdSensitiveAreaId = n.pbdSensitiveAreaId
    where d.pbdShift = 1
      and n.pbdShift = 0
      and 2019 between d.pbdValidFrom and d.pbdValidTo
      and 2019 between n.pbdValidFrom and n.pbdValidTo
)
-- insert into care.proof (prProofRegulationBaseInformationId, prProofRegulationStationId, prProofWardId, prValidFrom, prValidTo, prMonth, prDeptNumbers, prDeptNames, prSensitiveDomains, prSensitiveDomainId, prShift, prBeds, prCountShift, prNurse, prHelpeNurse, prPatientOccupancy, prCountShiftNotRespected, prPatientPerNurse, prCountHelpeNurseChargeable, prComment)
select prProofRegulationBaseInformationId,
       0,
       pwId,
       CONVERT(smalldatetime, '2019-' + CONVERT(varchar, prMonth) + '-01', 102)                                  as ValidFrom,
       CONVERT(smalldatetime, EOMONTH(CONVERT(smalldatetime, '2019-' + CONVERT(varchar, prMonth) + '-01',
                                                             102)))                                              as ValidTo,
       prMonth,
       dbo.DistinctList(prsFabNumber)                                                                            as depts,
       dbo.DistinctList(prsFabName)                                                                              as deptNames,
       dbo.DistinctList(sdName)                                                                                  as SensitiveAreas,
       min(seq * 100 + sensitiveDomainId) % 100                                                                  as SensitiveDomainId,
       prShift,
       prBeds,
       prCountShift,
       prNurse,
       prHelpeNurse,
       prPatientOccupancy,
       prCountShiftNotRespected,
       max(prPatientPerNurse),
       min(prCountHelpeNurseChargeable),
       prComment
from care.Proof
         join care.ProofRegulationStation on prProofRegulationStationId = prsId
         join care.ProofWard on prsIk = pwIk and prsLocationCode = pwLocationText and
                                lower(replace(prsStationName, ' ', '')) = lower(replace(pwName, ' ', ''))
         join care.listSensitiveDomain on prsSensitiveAreaId = sdId
         join sensitiveRanking on prsSensitiveAreaId = sensitiveDomainId
where prProofRegulationStationId > 0
--group by prProofRegulationBaseInformationId, pwId, prMonth, prShift // eigentlich sollten die Daten auf dieser Ebene eindeutig sein, sind sie aber nicht.
-- durch die eindeutige Gruppierung ergeben sich zusätzliche Datensätze. Lieber ein paar zuviel, als fehlende Info
group by prProofRegulationBaseInformationId, pwId, prMonth, prShift, prBeds, prCountShift, prNurse, prHelpeNurse,
         prPatientOccupancy, prCountShiftNotRespected, prComment
