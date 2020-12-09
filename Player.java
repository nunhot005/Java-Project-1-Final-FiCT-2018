
public class Player {

	public enum PlayerType {
		Healer, Tank, Samurai, BlackMage, Phoenix, Cherry
	};

	private PlayerType type; // Type of this player. Can be one of either Healer, Tank, Samurai, BlackMage,
	// or Phoenix
	private double maxHP; // Max HP of this player
	private double currentHP; // Current HP of this player
	private double atk; // Attack power of this player
	private double numSpecialTurns;
	private boolean isSleeping;
	private boolean isCursed;
	private boolean isAlive;
	private boolean isTaunting;
	private boolean TauntTheir;
	private int count = 0;
	private int internalcount = 0;

	private Player player_cursed = null;
	private String teamName;
	private String Row;
	private int position;

	/**
	 * Constructor of class Player, which initializes this player's type, maxHP,
	 * atk, numSpecialTurns, as specified in the given table. It also reset the
	 * internal turn count of this player.
	 *
	 * @param _type
	 */

	public Player(PlayerType _type) {
		type = _type;
		switch (_type) {
			case Healer:
				maxHP = 4790;
				atk = 238;
				numSpecialTurns = 4;
				isAlive = true;
				break;

			case Tank:
				maxHP = 5340;
				atk = 255;
				numSpecialTurns = 4;
				isAlive = true;
				break;

			case Samurai:
				maxHP = 4005;
				atk = 368;
				numSpecialTurns = 3;
				isAlive = true;
				break;

			case BlackMage:
				maxHP = 4175;
				atk = 303;
				numSpecialTurns = 4;
				isAlive = true;
				break;

			case Phoenix:
				maxHP = 4175;
				atk = 209;
				numSpecialTurns = 8;
				isAlive = true;
				break;

			case Cherry:
				maxHP = 3560;
				atk = 198;
				numSpecialTurns = 4;
				isAlive = true;
				break;
		}
		isSleeping = false;
		isCursed = false;
		isTaunting = false;
		currentHP = maxHP;
	}

	public void setTeam(String team) {
		this.teamName = team; // Use this to call from Arena
	}

	public String getTeam() {
		return this.teamName;
	}

	public void setRow(String row) {
		this.Row = row; // Set Hero from each Row
	}

	public String getRow() {
		return Row; // Return Hero from each Row
	}

	public void setPosition(int position) {
		this.position = position; // Use this to call from Arena
	}

	public int getPosition() {
		return this.position; // Return Position of Hero
	}

	public Player isTaunting(Player[][] theirTeam) {
		{
			for (int i = 0; i < 2; i++) {
				for (Player item : theirTeam[i]) {
					if (item.isTaunting) {
						return item;
					}
				}
			}
			return null;
		}
	}

	/**
	 * Returns the current HP of this player
	 *
	 * @return
	 */
	public double getCurrentHP() {
		// this.currentHP = currentHP;
		return this.currentHP;
	}

	/**
	 * Returns type of this player
	 *
	 * @return
	 */
	public Player.PlayerType getType() {

		return this.type;
	}

	/**
	 * Returns max HP of this player.
	 *
	 * @return
	 */
	public double getMaxHP() {
		// this.maxHP = maxHP;
		return this.maxHP;
	}

	/**
	 * Returns whether this player is sleeping.
	 *
	 * @return
	 */
	public boolean isSleeping() {
		return this.isSleeping;
	}

	/**
	 * Returns whether this player is being cursed.
	 *
	 * @return
	 */
	public boolean isCursed() {
		return this.isCursed;
	}

	/**
	 * Returns whether this player is alive (i.e. current HP > 0).
	 *
	 * @return
	 */
	public boolean isAlive() {
		if (currentHP > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether this player is taunting the other team.
	 *
	 * @return
	 */
	public boolean isTaunting() {
		return this.isTaunting;
	}

	public void attack(Player target) {
		target.currentHP -= this.atk;
		if (target.currentHP <= 0) {
			target.currentHP = 0;
			target.isAlive = false;
		}

	}

	public void revive(Player[][] myTeam) {
		Player died = myTeam[0][0];
		boolean whodead = false;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < myTeam[0].length; j++) {
				if (!myTeam[i][j].isAlive() && whodead == false) {
					died = myTeam[i][j];
					whodead = true;
					died.currentHP = died.currentHP + (died.maxHP * 0.3);
					died.isTaunting = false;
					died.isCursed = false;
					died.internalcount = 0;
					System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition()
							+ "]" + " {" + this.type.toString() + "}" + " Revives " + died.getTeam() + "["
							+ died.getRow() + "]" + "[" + died.getPosition() + "]" + " {" + died.type.toString() + "}");
					break;
				}
			}
		}
	}

	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam) {
		internalcount = 0;
		if (null != this.type)
			switch (this.type) {
				case Healer:
					this.getHeal(myTeam);
					break;
				case Tank:
					Player[][] Tank = this.Tank(theirTeam);
					break;
				case Samurai:

				case BlackMage:
					this.Cursed(theirTeam);
					break;
				case Phoenix:
					this.revive(myTeam);
					break;
				case Cherry:
					Player[][] FortuneCookie = this.Cookie(theirTeam);
					break;
				default:
					System.out.println("Error");
					break;
			}
	}

	public Player[][] Tank(Player[][] theirTeam) // Tank need this Function
	{
		System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition() + "]" + " {"
				+ this.type.toString() + "}" + " is Taunting");
		isTaunting = true;
		return theirTeam;
	}

	/**
	 * This method is called by Arena when it is this player's turn to take an
	 * action. By default, the player simply just "attack(target)". However, once
	 * this player has fought for "numSpecialTurns" rounds, this player must perform
	 * "useSpecialAbility(myTeam, theirTeam)" where each player type performs his
	 * own special move.
	 *
	 * @param arena
	 */
	public void takeAction(Arena arena) {
		if (this.type == PlayerType.BlackMage && player_cursed != null) {
			player_cursed.isCursed = false;
			player_cursed = null;
		}
		if (this.isTaunting) {
			this.isTaunting = false;
		}
		if (this.isSleeping) {
			this.isSleeping = false;
			return;
		}
		internalcount++;

		Player[][] myteam = null;
		Player[][] opp = null;
		if (arena.getMyTeam(this) == arena.getTeamA()) {
			myteam = arena.getTeamA();
			opp = arena.getTeamB();
		} else if (arena.getMyTeam(this) == arena.getTeamB()) {
			myteam = arena.getTeamB();
			opp = arena.getTeamA();
		}

		if (this.type == PlayerType.Healer && internalcount == this.numSpecialTurns) {
			useSpecialAbility(myteam, opp);
		} else if (this.type == PlayerType.Tank && internalcount == this.numSpecialTurns) {
			useSpecialAbility(myteam, opp);
		} else if (this.type == PlayerType.Samurai && internalcount == this.numSpecialTurns) {
			internalcount = 0;
			Player target = arena.findTarget(this);
			if (target == null) {
				return;
			}
			System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition() + "]" + " {"
					+ this.type.toString() + "}" + " Double-Slashes " + target.getTeam() + "[" + target.getRow() + "]"
					+ "[" + target.getPosition() + "]" + " {" + target.type.toString() + "}");
			attack(target);
			attack(target);
		} else if (this.type == PlayerType.BlackMage && internalcount == this.numSpecialTurns) {
			useSpecialAbility(myteam, opp);
		} else if (this.type == PlayerType.Phoenix && internalcount == this.numSpecialTurns) {
			useSpecialAbility(myteam, opp);
		} else if (this.type == PlayerType.Cherry && internalcount == this.numSpecialTurns) {
			useSpecialAbility(myteam, opp);
		} else {
			Player target = arena.findTarget(this);
			if (target != null) {
				attack(target);

				System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition() + "]"
						+ " {" + this.type.toString() + "}" + " Attacks " + target.getTeam() + "[" + target.getRow()
						+ "]" + "[" + target.getPosition() + "]" + " {" + target.type.toString() + "}");
			}
		}

		for (int i = 0; i < 2; i++) {
			for (Player item : opp[i]) {
				if (!item.isAlive()) {
					item.isCursed = false;
					item.isTaunting = false;
					item.isSleeping = false;
				}
			}
		}
		// System.out.println(internalcount);
	}

	/**
	 * This method overrides the default Object's toString() and is already
	 * implemented for you.
	 */
	@Override
	public String toString() {
		return "[" + this.type.toString() + " HP:" + this.currentHP + "/" + this.maxHP + " ATK:" + this.atk + "]["
				+ ((this.isCursed()) ? "C" : "") + ((this.isTaunting()) ? "T" : "") + ((this.isSleeping()) ? "S" : "")
				+ "]";
	}

	public void getHeal(Player[][] myTeam) {
		Player lowestOfHp = Heal(myTeam);
		if (lowestOfHp.isCursed) {
			return;
		}
		if (lowestOfHp.currentHP > 0 && lowestOfHp.currentHP < lowestOfHp.maxHP && lowestOfHp.isCursed == false) {
			lowestOfHp.currentHP += (0.25 * lowestOfHp.maxHP);

			if (lowestOfHp.currentHP > lowestOfHp.maxHP) {
				lowestOfHp.currentHP = lowestOfHp.maxHP;
			}
		}
	}

	public Player Heal(Player[][] myteam) {
		double mininum = 9999;
		Player target = null;

		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				for (Player item : myteam[i]) {
					if (item.getCurrentHP() > 0 && item.getCurrentHP() / (item.getMaxHP()) * 100 < mininum) {
						mininum = item.getCurrentHP() / (item.getMaxHP()) * 100;
						target = item;
					}
				}
				// A[Back][1] {Healer} Heals A[Front][1] {Samurai}

			} else {
				for (Player item : myteam[i]) {
					if (item.getCurrentHP() > 0 && item.getCurrentHP() / (item.getMaxHP()) * 100 < mininum) {
						mininum = item.getCurrentHP() / (item.getMaxHP()) * 100;
						target = item;
					}
				}
			}
		}
		System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition() + "]" + " {"
				+ this.type.toString() + "}" + " Heals " + target.getTeam() + "[" + target.getRow() + "]" + "["
				+ target.getPosition() + "]" + " {" + target.type.toString() + "}");
		return target;
	}

	public void setCurse(boolean curse) {
		this.isCursed = curse;
	}

	public Player Cursed(Player[][] theirTeam) {
		double mininum = 9999;
		Player target = null;
		boolean findcurse = false;
		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				// System.out.println("bobo 3");
				for (Player item : theirTeam[i]) {
					// System.out.println("bobo 2");
					// System.out.println(item.currentHP+" "+ mininum);
					if (item.isAlive() && (item.currentHP < mininum)) {
						mininum = item.currentHP;
						target = item;
						findcurse = true;
						// System.out.println("bobo");
						// System.out.println(item.getTeam());
					}
				}

			} else {
				for (Player item : theirTeam[i]) {
					if (item.isAlive() && item.currentHP < mininum) {
						mininum = item.currentHP;
						target = item;
						findcurse = true;
					}
				}
				// System.out.println("bobo");
				// System.out.println(target.getTeam());
			}
		}
		if (target != null) {
			target.setCurse(true);
			player_cursed = target;
			System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition() + "]" + " {"
					+ this.type.toString() + "}" + " Curses " + target.getTeam() + "[" + target.getRow() + "]" + "["
					+ target.getPosition() + "]" + " {" + target.type.toString() + "}");
		}
		return target;
	}

	public Player[][] Cookie(Player[][] theirTeam) // Cherry(Cherprang) use this to sing to opsTeam
	{
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < theirTeam[0].length; j++) {
				if (theirTeam[i][j].isAlive()) {
					System.out.println("# " + this.getTeam() + "[" + this.getRow() + "]" + "[" + this.getPosition()
							+ "]" + " {" + this.type.toString() + "}" + " Feeds a Fortune Cookie to "
							+ theirTeam[i][j].getTeam() + "[" + theirTeam[i][j].getRow() + "]" + "["
							+ theirTeam[i][j].getPosition() + "]" + " {" + theirTeam[i][j].type.toString() + "}");
					theirTeam[i][j].isSleeping = true;
				}
			}
		}
		return theirTeam;
	}

}
