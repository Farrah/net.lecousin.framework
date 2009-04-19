package net.lecousin.framework.ui.eclipse;

import net.lecousin.framework.application.Application;

public enum Local {

	Cancel("Cancel", "Annuler"),
	Close("Close", "Fermer"),
	Filters("Filters", "Filtres"),
	Ignore("Ignore", "Ignorer"),
	No("No", "Non"),
	Ok("Ok", "Ok"),
	on("on", "sur"),
	Page("Page", "page"),
	Select_columns("Select columns", "S�lectionner les colonnes"),
	to__date("to", "au"),
	to__time("to", "�"),
	Yes("Yes", "Oui"),
	
	Elapsed_time("Elapsed time", "Temps �coul�"),
	Estimated_remaining_time("Estimated time left", "Temps restant �stim�"),
	Operation_in_progress("Operation in progress", "Op�ration en cours"),
	
	;
	
	private Local(String english, String french) {
		this.english = english;
		this.french = french;
	}
	private String english;
	private String french;
	@Override
	public java.lang.String toString() {
		switch (Application.language) {
		case FRENCH: return french;
		default: return english;
		}
	}
}
