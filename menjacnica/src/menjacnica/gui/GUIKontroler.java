package menjacnica.gui;

import java.awt.EventQueue;
import java.awt.Frame;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.w3c.dom.views.DocumentView;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {

	private static MenjacnicaInterface menjacnica;
	private static MenjacnicaGUI glavniProzor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menjacnica = new Menjacnica();
					glavniProzor = new MenjacnicaGUI();
					glavniProzor.setVisible(true);
					glavniProzor.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setVisible(true);
		prozor.setLocationRelativeTo(null);
	}

	public static void prikaziObrisiKursGUI() {

		if (glavniProzor.getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (glavniProzor.getTable().getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(model.vratiValutu(glavniProzor.getTable().getSelectedRow()));
			prozor.setLocationRelativeTo(null);
			prozor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (glavniProzor.getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (glavniProzor.getTable().getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(model.vratiValutu(glavniProzor.getTable().getSelectedRow()));
			prozor.setLocationRelativeTo(null);
			prozor.setVisible(true);
		}
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(glavniProzor, "Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(glavniProzor, "Da li ZAISTA zelite da izadjete iz apliacije",
				"Izlazak", JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(glavniProzor);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				menjacnica.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(glavniProzor);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnica.ucitajIzFajla(file.getAbsolutePath());
				glavniProzor.prikaziSveValute();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void unesiKurs(String naziv, String skraceniNaziv, int sifra, String prodajni, String kupovni,
			String srednji) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(Double.parseDouble(prodajni));
			valuta.setKupovni(Double.parseDouble(kupovni));
			valuta.setSrednji(Double.parseDouble(srednji));

			// Dodavanje valute u kursnu listu
			menjacnica.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			glavniProzor.prikaziSveValute();

			// Zatvaranje DodajValutuGUI prozora
			// dispose();

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(new DodajKursGUI(), e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void obrisiValutu(Valuta valuta) {
		try {
			menjacnica.obrisiValutu(valuta);
			glavniProzor.prikaziSveValute();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(new ObrisiKursGUI(valuta), e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static double izvrsiZamenu(Valuta valuta, boolean prodaja, String iznos) {
		double doubleIznos = 0;
		try {
			doubleIznos = Double.parseDouble(iznos);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(new IzvrsiZamenuGUI(valuta), e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
		return menjacnica.izvrsiTransakciju(valuta, prodaja, doubleIznos);
	}

	public static LinkedList<Valuta> vratiKursnuListu() {
		return menjacnica.vratiKursnuListu();
	}
}
