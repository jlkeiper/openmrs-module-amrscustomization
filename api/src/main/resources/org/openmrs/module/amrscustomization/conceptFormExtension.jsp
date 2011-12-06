
<div id="addAnswer" style="display: none">
	<div id="addAnswerError">No Concepts Found - Invalid Concept Id</div>
	<div id="addConceptOrDrug">
		<h3><a href="#">Find Concept(s)</a></h3>
		<div><input type="text" name="newAnswerConcept" id="newAnswerConcept" size="20"/></div>
		<h3><a href="#">Find Concept Drugs</a></h3>
		<div><input type="text" name="newAnswerDrug" id="newAnswerDrug" size="20"/></div>
	</div>
	<input type="hidden" name="newAnswerId" id="newAnswerId"/>
	<input type="hidden" name="newAnswerType" id="newAnswerType"/>
</div>

<style>
	#addAnswerError{ margin-bottom: 0.5em; border: 1px dashed black; background: #FAA; line-height: 2em; text-align: center; display: none; }
</style>

<script src="../moduleResources/amrscustomization/OpenmrsAutoComplete.js"></script>

<script type="text/javascript">
	$j(document).ready(function(){
		// create the Add Answer dialog
		$j('#addAnswer').dialog({
			autoOpen: false,
			modal: true,
			title: 'Choose an Answer',
			width: 'auto',
			open: function() {
				$j("#newAnswerConcept").val(""); 
				$j("#newAnswerDrug").val(""); 
				$j("input[name=newAnswerId]").val(""); 
				$j("input[name=newAnswerType]").val(""); },
			close: function() { 
				$j("#addAnswerError").hide(); 
				$j("#newAnswerConcept").autocomplete("close"); 
				$j("#newAnswerDrug").autocomplete("close"); },
			buttons: { 'Add': function() { handleAddAnswer(); },
					   'Cancel': function() { $j(this).dialog("close"); }
			}
		});
		
		// set up accordion for adding concepts or drugs
		$j('#addConceptOrDrug').accordion({
			autoHeight: false,
			change: function(event, ui){
				// only hide the error if it is visible
				$j("#addAnswerError:visible").hide('blind'); 
				// clear previously selected data
				ui.oldContent.find('input').val("");
				$j("input[name=newAnswerId]").val("");
				$j("input[name=newAnswerType]").val("");
				// focus on the newly revealed input
				ui.newContent.find('input').focus();
			}
		});
		
		// concept answer autocompletes
		var answerCallback = new CreateCallback();
		var autoAddAnswerConcept = new AutoComplete("newAnswerConcept", answerCallback.conceptCallback(), {
			select: function(event, ui) {
				$j("input[name=newAnswerId]").val(ui.item.object.conceptId);
				$j("input[name=newAnswerType]").val("concept");
			}
		});
		var autoAddAnswerDrug = new AutoComplete("newAnswerDrug", answerCallback.drugCallback(), {
			select: function(event, ui) {
				$j("input[name=newAnswerId]").val(ui.item.object.drugId);
				$j("input[name=newAnswerType]").val("drug");
			}
		});
		
		// custom for the module -- replace the add button
		setTimeout("replaceAddButton()", 1000);
	});

	function replaceAddButton(){
		var htmlToAdd = '<input type="button" value="Add" class="smallButton" onClick="addAnswer();"/><br/>';
		$j("tr#codedDatatypeRow table td.buttons span:first").remove();
		$j("tr#codedDatatypeRow table td.buttons").prepend(htmlToAdd);
	}

	function addAnswer() {
		$j('#addAnswer').dialog('open');
		$j('#addConceptOrDrug input:visible').focus();
	}
	
	function handleNewAnswerObject(newAnswer) {
		var nameListBox = document.getElementById("answerNames");
		var idListBox = document.getElementById("answerIds");
		var options = nameListBox.options;
		addOption(newAnswer, options);
		copyIds(nameListBox.id, idListBox.id, ' ');
		$j("#addAnswer").dialog('close');
	}

	function handleAddAnswer() {
		var newAnswerId = $j("input[name=newAnswerId]").val();
		var newAnswerType = $j("input[name=newAnswerType]").val();
		if (newAnswerId == "" || newAnswerType == "") {
			$j("#addAnswerError").show('highlight', 1000);
			return;
		}
		
		if (newAnswerType == "concept") {
			DWRConceptService.getConcept(newAnswerId, handleNewAnswerObject);
		} else if (newAnswerType == "drug") {
			DWRConceptService.getDrug(newAnswerId, handleNewAnswerObject);
		}
	}

</script>
