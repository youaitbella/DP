use DataPortalDev

/*
delete care.proof where prProofRegulationStationId = 0

truncate table care.mapProofWardDeptSensitiveDomain

delete care.proofWardDept
DBCC CHECKIDENT ([care.proofWardDept], reseed, 1)

delete care.proofWard where pwId > 0
DBCC CHECKIDENT ([care.proofWard], reseed, 1)

*/


--insert into care.ProofWard (pwIk, pwLocationNumber, pwLocationText, pwName)
select prsIk, 0, prsLocationCode, prsStationName
from care.ProofRegulationStation
group by prsIk, prsLocationCode, prsStationName

--select * from care.ProofWardDept

--insert into care.ProofWardDept (pwdProofWardId, pwdValidFrom, pwdDeptNumbers, pwdDeptNames)
select pwId, validFrom, depts, deptNames
from (
	select pwId, prsSensitiveAreaId, depts, deptNames, convert(smalldatetime, '2019-' + cast(min(prsmMonth) as varchar) +'-01', 102) as validFrom
	from (
		select pwId, prsmMonth, prsSensitiveAreaId, dbo.ConcatenateSort(prsFabNumber) as depts, dbo.ConcatenateSort(prsFabName) as deptNames
		from care.ProofRegulationStation
		join care.MapProofRegulationStationMonth on prsId = prsmProofRegulationStationId
		join care.proofWard on prsIk = pwIk and prsLocationCode = pwLocationText and prsStationName = pwName
		group by pwId, prsmMonth, prsSensitiveAreaId
	) base
	group by pwId, prsSensitiveAreaId, depts, deptNames
) d
group by pwId, depts, deptNames, validFrom

--insert into care.mapProofWardDeptSensitiveDomain (proofWardDeptId, sensitiveDomainId)
select pwdId, prsSensitiveAreaId
from (
	select pwId, prsSensitiveAreaId, depts, deptNames, convert(smalldatetime, '2019-' + cast(min(prsmMonth) as varchar) +'-01', 102) as validFrom
	from (
		select pwId, prsmMonth, prsSensitiveAreaId, dbo.ConcatenateSort(prsFabNumber) as depts, dbo.ConcatenateSort(prsFabName) as deptNames
		from care.ProofRegulationStation
		join care.MapProofRegulationStationMonth on prsId = prsmProofRegulationStationId
		join care.proofWard on prsIk = pwIk and prsLocationCode = pwLocationText and prsStationName = pwName
		group by pwId, prsmMonth, prsSensitiveAreaId
	) base
	group by pwId, prsSensitiveAreaId, depts, deptNames
) d
join care.ProofWardDept on pwId =  pwdProofWardId and validFrom = pwdValidFrom


--insert into care.proof (prProofRegulationBaseInformationId, prProofRegulationStationId, prProofWardId, prMonth, prShift, prBeds, prMaxShiftCount, prCountShift, prNurse, prHelpeNurse, prPatientOccupancy, prCountShiftNotRespected, prPatientPerNurse, prCountHelpeNurseChargeable, prComment)
select prProofRegulationBaseInformationId, 0, pwId, prMonth, prShift, prBeds, prMaxShiftCount, prCountShift, prNurse, prHelpeNurse, prPatientOccupancy, prCountShiftNotRespected, max(prPatientPerNurse), min(prCountHelpeNurseChargeable), prComment
from care.Proof
join care.ProofRegulationStation on prProofRegulationStationId = prsId
join care.ProofWard  on prsIk = pwIk and prsLocationCode = pwLocationText and prsStationName = pwName
where prProofRegulationStationId > 0 
--group by prProofRegulationBaseInformationId, pwId, prMonth, prShift // eigentlich sollten die Daten auf dieser Ebene eindeutig sein, sind sie aber nicht.
-- durch die eindeutige Gruppierung ergeben sich zus�tzliche Datens�tze. Lieber ein paar zuviel, als fehlende Info
group by prProofRegulationBaseInformationId, pwId, prMonth, prShift, prBeds, prMaxShiftCount, prCountShift, prNurse, prHelpeNurse, prPatientOccupancy, prCountShiftNotRespected,  prComment
