# Game Plugin

Gun toml template:

```toml
id = "internet"
displayName = "§aDisplay Test"
material = "IRON_HOE"
customModelData = 0

type = "AR"
ammoType = "AR"

shootParticle = "FLAME"
bulletsPerShot = 1
range = 50.0
zoom = 5
reloadTimeInTicks = 30
fireRate = 300
maxAmmo = 30

headDamage = 8.0
bodyDamage = 5.0
legDamage = 3.0

spread = 2.0
sneakSpread = 0.5

[[sounds]]
type = "SHOOT"
sound = "shoot.ar"
volume = 1.0
pitch = 2.0
surroundingDistance = 20
```