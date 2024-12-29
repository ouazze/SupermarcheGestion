import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SupermarcheGestion {
    public static void main(String[] args) {
        new GestionnaireStockUI();
    }
}

class GestionnaireStockUI {
    private JFrame frame;
    private JTextArea stockArea;
    private JTextField produitField;
    private JTextField quantiteField;
    private Map<String, Integer> stock;
    private JPanel graphPanel;
    private Map<String, Color> couleursProduits;

    public GestionnaireStockUI() {
        stock = new HashMap<>();
        couleursProduits = new HashMap<>();
        initialiserUI();
    }

    private void initialiserUI() {
        frame = new JFrame("SuperMarché - Gestion de Stock");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel("SuperMarché - Gestion de Stock", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Zone d'affichage du stock
        stockArea = new JTextArea();
        stockArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(stockArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panneau des opérations
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Produit :"));
        produitField = new JTextField();
        panel.add(produitField);

        panel.add(new JLabel("Quantité :"));
        quantiteField = new JTextField();
        panel.add(quantiteField);

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterProduit();
            }
        });
        panel.add(ajouterButton);

        JButton retirerButton = new JButton("Retirer");
        retirerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retirerProduit();
            }
        });
        panel.add(retirerButton);

        frame.add(panel, BorderLayout.SOUTH);

        // Panneau du graphique
        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dessinerGraphique(g);
            }
        };
        graphPanel.setPreferredSize(new Dimension(800, 200));
        frame.add(graphPanel, BorderLayout.EAST);

        // Afficher la fenêtre
        frame.setVisible(true);
    }

    private void ajouterProduit() {
        String produit = produitField.getText().trim();
        String quantiteStr = quantiteField.getText().trim();

        if (produit.isEmpty() || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Veuillez remplir les champs Produit et Quantité.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteStr);
            stock.put(produit, stock.getOrDefault(produit, 0) + quantite);

            // Assigner une couleur unique au produit si ce n'est pas déjà fait
            if (!couleursProduits.containsKey(produit)) {
                couleursProduits.put(produit, genererCouleurAleatoire());
            }

            afficherStock();
            produitField.setText("");
            quantiteField.setText("");
            graphPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retirerProduit() {
        String produit = produitField.getText().trim();
        String quantiteStr = quantiteField.getText().trim();

        if (produit.isEmpty() || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Veuillez remplir les champs Produit et Quantité.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!stock.containsKey(produit)) {
            JOptionPane.showMessageDialog(frame, "Le produit n'existe pas dans le stock.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteStr);
            int quantiteActuelle = stock.get(produit);

            if (quantite >= quantiteActuelle) {
                stock.remove(produit);
                couleursProduits.remove(produit); // Supprimer la couleur associée
            } else {
                stock.put(produit, quantiteActuelle - quantite);
            }

            afficherStock();
            produitField.setText("");
            quantiteField.setText("");
            graphPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherStock() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Stock Actuel ---\n");

        if (stock.isEmpty()) {
            sb.append("Le stock est vide.\n");
        } else {
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }
        }

        stockArea.setText(sb.toString());
    }

    private void dessinerGraphique(Graphics g) {
        if (stock.isEmpty()) {
            g.drawString("Aucun produit dans le stock.", 10, 20);
            return;
        }

        int width = graphPanel.getWidth();
        int height = graphPanel.getHeight();

        int x = 10;
        int barWidth = (width - 20) / stock.size();
        int maxQuantite = stock.values().stream().max(Integer::compare).orElse(1);

        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            String produit = entry.getKey();
            int quantite = entry.getValue();

            int barHeight = (int) ((double) quantite / maxQuantite * (height - 30));

            g.setColor(couleursProduits.get(produit));
            g.fillRect(x, height - barHeight - 20, barWidth - 5, barHeight);

            g.setColor(Color.BLACK);
            g.drawString(produit, x, height - 5);

            x += barWidth;
        }
    }

    private Color genererCouleurAleatoire() {
        return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }
}
